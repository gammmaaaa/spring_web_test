package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.dto.TransactionResult;
import ru.t1.java.demo.kafka.handler.TransactionResultStatusFactory;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionResultConsumer {
    private final TransactionService transactionService;
    private final TransactionResultStatusFactory transactionResultStatusFactory;

    @KafkaListener(id = "${t1.kafka.consumer.transaction_result.group-id}",
            topics = "${t1.kafka.topic.transaction_result}",
            containerFactory = "kafkaListenerContainerTransactionResultFactory")
    @Transactional
    public void listener(@Payload TransactionResult message,
                         Acknowledgment ack) {
        log.info("Transaction result consumer: Обработка новых сообщений");

        try {
            Transaction transaction = transactionService.getTransactionById(message.getTransactionId());
            transactionResultStatusFactory.getImpl(message.getTransactionStatus()).handle(transaction);
            transactionService.updateTransaction(transaction);
        } finally {
            ack.acknowledge();
        }

        log.info("Transaction result consumer: записи обработаны");
    }
}
