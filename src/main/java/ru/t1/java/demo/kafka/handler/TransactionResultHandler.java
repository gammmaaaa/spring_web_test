package ru.t1.java.demo.kafka.handler;

import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.TransactionStatusEnum;

public interface TransactionResultHandler {
    void handle(Transaction transaction);

    TransactionStatusEnum getStatus();
}
