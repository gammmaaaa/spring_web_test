package ru.t1.java.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.t1.java.demo.dto.AccountDTO;
import ru.t1.java.demo.dto.ClientDTO;
import ru.t1.java.demo.dto.TransactionDTO;
import ru.t1.java.demo.dto.TransactionResult;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.kafka.MessageDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@SuppressWarnings({"unchecked", "rawtypes"})
public class KafkaClientConfig<T> {
    @Value("${t1.kafka.consumer.client.group-id}")
    private String groupId;
    @Value("${t1.kafka.bootstrap.server}")
    private String servers;
    @Value("${t1.kafka.session.timeout.ms}")
    private String sessionTimeout;
    @Value("${t1.kafka.max.partition.fetch.bytes}")
    private String maxPartitionFetchBytes;
    @Value("${t1.kafka.max.poll.records}")
    private String maxPollRecords;
    @Value("${t1.kafka.max.poll.interval.ms}")
    private String maxPollIntervalsMs;
    @Value("${t1.kafka.heartbeat.interval.ms}")
    private String heartbeatInterval;
    @Value("${t1.kafka.topic.client_id_registered}")
    private String clientTopic;


    private ConsumerFactory<String, T> consumerListenerFactory(String valueDefaultType) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueDefaultType);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalsMs);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, MessageDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, MessageDeserializer.class);

        DefaultKafkaConsumerFactory factory = new DefaultKafkaConsumerFactory<String, T>(props);
        factory.setKeyDeserializer(new StringDeserializer());

        return factory;
    }

    ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(String valueDefaultType) {
        ConsumerFactory<String, T> consumerFactory = consumerListenerFactory(valueDefaultType);
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, ClientDTO> kafkaListenerContainerClientFactory() {
        return (ConcurrentKafkaListenerContainerFactory<String, ClientDTO>) kafkaListenerContainerFactory("ru.t1.java.demo.dto.ClientDTO");
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, AccountDTO> kafkaListenerContainerAccountFactory() {
        return (ConcurrentKafkaListenerContainerFactory<String, AccountDTO>) kafkaListenerContainerFactory("ru.t1.java.demo.dto.AccountDTO");
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TransactionDTO> kafkaListenerContainerTransactionFactory() {
        return (ConcurrentKafkaListenerContainerFactory<String, TransactionDTO>) kafkaListenerContainerFactory("ru.t1.java.demo.dto.TransactionDTO");
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TransactionResult> kafkaListenerContainerTransactionResultFactory() {
        return (ConcurrentKafkaListenerContainerFactory<String, TransactionResult>) kafkaListenerContainerFactory("ru.t1.java.demo.dto.TransactionResult");
    }

    private void factoryBuilder(ConsumerFactory<String, T> consumerFactory, ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(5000);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(errorHandler());
    }

    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.error(" RetryListeners message = {}, offset = {} deliveryAttempt = {}", ex.getMessage(), record.offset(), deliveryAttempt);
        });
        return handler;
    }

    @Bean("client")
    @Primary
    public KafkaTemplate<String, Object> kafkaClientTemplate(@Qualifier("producerClientFactory") ProducerFactory<String, Object> producerPatFactory) {
        return new KafkaTemplate<>(producerPatFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaClientProducer producerClient(@Qualifier("client") KafkaTemplate<String, Object> template) {
        template.setDefaultTopic(clientTopic);
        return new KafkaClientProducer(template);
    }

    @Bean("producerClientFactory")
    public ProducerFactory<String, Object> producerClientFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }
}
