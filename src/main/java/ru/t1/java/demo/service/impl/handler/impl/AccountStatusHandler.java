package ru.t1.java.demo.service.impl.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.dto.TransactionForAccept;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.mapper.TransactionMapper;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.impl.handler.TransactionHandler;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static ru.t1.java.demo.model.enums.AccountStatusEnum.OPEN;
import static ru.t1.java.demo.model.enums.TransactionStatusEnum.REQUESTED;

@Service
@RequiredArgsConstructor
public class AccountStatusHandler extends TransactionHandler {

    private final TransactionRepository transactionRepository;
    private final KafkaClientProducer kafkaClientProducer;
    private final TransactionMapper transactionMapper;

    @Value("${t1.kafka.topic.transaction_accept}")
    private String transactionAcceptTopic;

    @Override
    @Transactional
    public void handle(Transaction transaction) throws TransactionException {
        if (isAccountOpen(transaction)) {
            BigDecimal balance = transaction.getAccount().getBalance();
            BigDecimal newBalance = balance.subtract(transaction.getAmount());
            transaction.setTimeTransaction(LocalDateTime.now());
            transaction.getAccount().setBalance(newBalance);
            transaction.setTransactionStatus(REQUESTED);
            transaction = transactionRepository.save(transaction);
            TransactionForAccept transactionForAccept = transactionMapper.toAccept(transaction);
            kafkaClientProducer.sendTo(transactionAcceptTopic, transactionForAccept);
        } else {
            throw new TransactionException("Your account status is: " + transaction.getAccount().getAccountStatus().name());
        }
    }

    private boolean isAccountOpen(Transaction transaction) {
        return OPEN.equals(transaction.getAccount().getAccountStatus());
    }
}
