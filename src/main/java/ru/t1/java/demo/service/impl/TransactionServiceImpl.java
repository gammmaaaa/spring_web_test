package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.service.impl.handler.impl.TransactionHandlerChain;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionHandlerChain transactionHandlerChain;


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
        transactionHandlerChain.create().handle(transaction);
        return transaction;
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
