package ru.t1.java.demo.service;

import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();

    Transaction getTransactionById(long id);

    Transaction saveTransaction(Transaction transaction) throws TransactionException;

    void deleteTransactionById(long id);
}
