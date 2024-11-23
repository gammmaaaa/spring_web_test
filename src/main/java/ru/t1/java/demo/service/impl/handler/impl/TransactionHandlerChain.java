package ru.t1.java.demo.service.impl.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.service.impl.handler.TransactionHandler;

@Component
@RequiredArgsConstructor
public class TransactionHandlerChain {

    private final AccountStatusHandler accountStatusHandler;
    private final BlockedClientHandler blockedClientHandler;
    private final NullClientBlockHandler nullClientBlockHandler;


    public TransactionHandler create() {
        return TransactionHandler.link(
                nullClientBlockHandler,
                blockedClientHandler,
                accountStatusHandler
        );
    }
}
