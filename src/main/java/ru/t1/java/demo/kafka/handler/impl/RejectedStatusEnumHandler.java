package ru.t1.java.demo.kafka.handler.impl;

import org.springframework.stereotype.Service;
import ru.t1.java.demo.kafka.handler.TransactionResultHandler;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.TransactionStatusEnum;

import java.math.BigDecimal;

import static ru.t1.java.demo.model.enums.TransactionStatusEnum.REJECTED;

@Service
public class RejectedStatusEnumHandler implements TransactionResultHandler {
    @Override
    public void handle(Transaction transaction) {
        transaction.setTransactionStatus(getStatus());
        BigDecimal transactionAmount = transaction.getAmount();
        BigDecimal accountBalance = transaction.getAccount().getBalance();
        BigDecimal newAccountBalance = accountBalance.add(transactionAmount);
        transaction.getAccount().setBalance(newAccountBalance);
    }

    @Override
    public TransactionStatusEnum getStatus() {
        return REJECTED;
    }
}
