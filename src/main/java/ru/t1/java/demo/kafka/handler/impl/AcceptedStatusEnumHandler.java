package ru.t1.java.demo.kafka.handler.impl;

import org.springframework.stereotype.Service;
import ru.t1.java.demo.kafka.handler.TransactionResultHandler;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.TransactionStatusEnum;

import static ru.t1.java.demo.model.enums.TransactionStatusEnum.ACCEPTED;

@Service
public class AcceptedStatusEnumHandler implements TransactionResultHandler {
    @Override
    public void handle(Transaction transaction) {
        transaction.setTransactionStatus(getStatus());
    }

    @Override
    public TransactionStatusEnum getStatus() {
        return ACCEPTED;
    }
}
