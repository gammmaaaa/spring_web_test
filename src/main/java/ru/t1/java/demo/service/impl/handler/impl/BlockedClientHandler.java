package ru.t1.java.demo.service.impl.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.impl.handler.TransactionHandler;

import java.time.LocalDateTime;
import java.util.List;

import static ru.t1.java.demo.model.enums.AccountStatusEnum.ARRESTED;
import static ru.t1.java.demo.model.enums.TransactionStatusEnum.REJECTED;

@Service
@RequiredArgsConstructor
public class BlockedClientHandler extends TransactionHandler {

    private final TransactionRepository transactionRepository;

    @Value("${t1.count.transactions-rejected}")
    private Long countTransactionsRejected;

    @Override
    public void handle(Transaction transaction) throws TransactionException {
        if (isClientBlocked(transaction)) {
            List<Transaction> transactionsAfterBlocked =
                    transactionRepository.findTransactionsAfterClientBlock(transaction.getAccount().getClient().getId());
            if (transactionsAfterBlocked.size() + 1 >= countTransactionsRejected) {
                transaction.getAccount().setAccountStatus(ARRESTED);
            }
            transaction.setTransactionStatus(REJECTED);
            transaction.setTimeTransaction(LocalDateTime.now());
            transaction = transactionRepository.save(transaction);
        } else {
            handleNext(transaction);
        }
    }

    private boolean isClientBlocked(Transaction transaction) {
        return Boolean.TRUE.equals(transaction.getAccount().getClient().getBlockedFor());
    }
}
