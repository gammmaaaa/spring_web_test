package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.dto.TransactionForAccept;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.mapper.TransactionMapper;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.AccountStatusEnum;
import ru.t1.java.demo.model.enums.TransactionStatusEnum;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final KafkaClientProducer kafkaClientProducer;
    private final TransactionMapper transactionMapper;

    @Value("${t1.kafka.topic.transaction_accept}")
    private String transactionAcceptTopic;

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Transaction saveTransaction(Transaction transaction) throws TransactionException {
        if (AccountStatusEnum.OPEN.equals(transaction.getAccount().getAccountStatus())) {

            BigDecimal balance = transaction.getAccount().getBalance();
            BigDecimal diff = balance.subtract(transaction.getAmount());

            transaction.setTimeTransaction(LocalDateTime.now());
            transaction.getAccount().setBalance(diff);
            transaction.setTransactionStatus(TransactionStatusEnum.REQUESTED);
            Transaction transactionSaved = transactionRepository.save(transaction);

            TransactionForAccept transactionForAccept = transactionMapper.toAccept(transactionSaved);
            kafkaClientProducer.sendTo(transactionAcceptTopic, transactionForAccept);

            return transactionSaved;

        } else {
            throw new TransactionException("Your account status is: " + transaction.getAccount().getAccountStatus().name());
        }
    }

    @Transactional
    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransactionById(long id) {
        transactionRepository.deleteById(id);
    }
}
