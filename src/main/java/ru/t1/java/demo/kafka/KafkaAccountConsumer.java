package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.AccountDTO;
import ru.t1.java.demo.mapper.AccountMapper;
import ru.t1.java.demo.service.AccountService;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaAccountConsumer {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @KafkaListener(id = "${t1.kafka.consumer.account.group-id}",
            topics = "${t1.kafka.topic.accounts}",
            containerFactory = "kafkaListenerContainerAccountFactory")
    public void listener(@Payload AccountDTO message,
                         Acknowledgment ack) {
        log.info("Account consumer: Обработка новых сообщений");

        try {
            accountService.saveNewAccount(accountMapper.toEntity(message));
        } finally {
            ack.acknowledge();
        }

        log.info("Account consumer: записи обработаны");
    }
}
