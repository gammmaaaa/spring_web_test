package ru.t1.java.demo.service.impl.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.impl.handler.TransactionHandler;
import ru.t1.java.demo.web.CheckWebClient;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.t1.java.demo.model.enums.AccountStatusEnum.BLOCKED;

@Service
@RequiredArgsConstructor
public class NullClientBlockHandler extends TransactionHandler {
    private final CheckWebClient checkWebClient;

    @Override
    public void handle(Transaction transaction) throws TransactionException {
        if (isClientBlockNull(transaction)) {
            checkWebClient.check(transaction.getAccount().getClient().getId()).ifPresent(checkResponse -> {
                Boolean isBlocked = checkResponse.getBlocked();
                transaction.getAccount().getClient().setBlockedFor(isBlocked);
                if (isBlocked) {
                    transaction.getAccount().getClient().setBlockedTime(LocalDateTime.now());
                    transaction.getAccount().setAccountStatus(BLOCKED);
                }
            });
        }

        handleNext(transaction);
    }

    private boolean isClientBlockNull(Transaction transaction) {
        return Objects.isNull(transaction.getAccount().getClient().getBlockedFor());
    }
}
