package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.dto.TransactionDTO;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.mapper.TransactionMapper;
import ru.t1.java.demo.service.TransactionService;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionConsumer {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @KafkaListener(id = "${t1.kafka.consumer.transaction.group-id}",
            topics = "${t1.kafka.topic.transactions}",
            containerFactory = "kafkaListenerContainerTransactionFactory")
    @Transactional
    public void listener(@Payload TransactionDTO message,
                         Acknowledgment ack) throws TransactionException {
        log.info("Transaction consumer: Обработка новых сообщений");

        try {
            transactionService.saveTransaction(transactionMapper.toEntity(message));
        } finally {
            ack.acknowledge();
        }

        log.info("Transaction consumer: записи обработаны");
    }

}
