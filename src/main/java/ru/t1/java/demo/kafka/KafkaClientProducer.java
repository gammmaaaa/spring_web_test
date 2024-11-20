package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaClientProducer {

    private final KafkaTemplate template;

    public void send(Long clientId) {
        try {
            template.sendDefault(UUID.randomUUID().toString(), clientId).get();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }

    public void sendTo(String topic, Object o) {
        try {
            template.send(topic, o);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }

    public void sendToWithHeader(String topic, Object o, String... headers) {
        var producerRecord =
                new ProducerRecord(topic, null, UUID.randomUUID().toString(), o,
                        Arrays.stream(headers).map(header -> new RecordHeader(header, header.getBytes())).toList());
        template.send(producerRecord);

    }
}
