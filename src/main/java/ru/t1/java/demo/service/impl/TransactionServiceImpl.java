package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;
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

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public Transaction saveTransaction(Transaction transaction) throws TransactionException {
        BigDecimal balance = transaction.getAccount().getBalance();

        BigDecimal diff = balance.subtract(transaction.getAmount());
        if (diff.compareTo(BigDecimal.ZERO) > 0) {
            transaction.setTimeTransaction(LocalDateTime.now());
            transaction.getAccount().setBalance(diff);
            return transactionRepository.save(transaction);
        }

        throw new TransactionException("Not enough funds on account");
    }

    @Override
    public void deleteTransactionById(long id) {
        transactionRepository.deleteById(id);
    }
}
