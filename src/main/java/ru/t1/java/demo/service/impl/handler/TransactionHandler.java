package ru.t1.java.demo.service.impl.handler;

import lombok.RequiredArgsConstructor;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;

@RequiredArgsConstructor
public abstract class TransactionHandler {
    private TransactionHandler next;

    public static TransactionHandler link(TransactionHandler first, TransactionHandler... chain) {
        TransactionHandler head = first;
        for (TransactionHandler nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }

        return first;
    }

    protected void handleNext(Transaction transaction) throws TransactionException {
        if (next != null) {
            next.handle(transaction);
        }
    }

    public abstract void handle(Transaction transaction) throws TransactionException;
}
