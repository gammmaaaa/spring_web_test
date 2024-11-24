package ru.t1.java.demo.service.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.dto.CheckResponse;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.impl.handler.impl.NullClientBlockHandler;
import ru.t1.java.demo.web.CheckWebClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.t1.java.demo.model.enums.AccountStatusEnum.BLOCKED;

@ExtendWith(MockitoExtension.class)
public class NullClientBlockHandlerTest {

    @Mock
    private CheckWebClient checkWebClient;

    @InjectMocks
    private NullClientBlockHandler nullClientBlockHandler;

    private Transaction transaction;
    private Account account;
    private Client client;

    @BeforeEach
    void setUp() {
        client = Client.builder().build();
        account = Account.builder()
                .client(client)
                .build();
        transaction = Transaction.builder()
                .account(account)
                .build();
    }

    @Test
    void testHandle_ClientIsNotBlocked() throws TransactionException {
        client.setBlockedFor(null);
        CheckResponse checkResponse = CheckResponse.builder()
                .blocked(false)
                .build();

        when(checkWebClient.check(client.getId())).thenReturn(Optional.of(checkResponse));

        nullClientBlockHandler.handle(transaction);

        assertFalse(client.getBlockedFor());
        assertNotEquals(BLOCKED, account.getAccountStatus());
        verify(checkWebClient).check(client.getId());
    }

    @Test
    void testHandle_ClientIsBlocked() throws TransactionException {
        client.setBlockedFor(null);
        CheckResponse checkResponse = CheckResponse.builder()
                .blocked(true)
                .build();

        when(checkWebClient.check(client.getId())).thenReturn(Optional.of(checkResponse));

        nullClientBlockHandler.handle(transaction);

        assertTrue(client.getBlockedFor());
        assertNotNull(client.getBlockedTime());
        assertEquals(BLOCKED, account.getAccountStatus());
        verify(checkWebClient).check(client.getId());
    }

    @Test
    void testHandle_ClientAlreadyBlocked() throws TransactionException {
        client.setBlockedFor(true);
        account.setAccountStatus(BLOCKED);

        nullClientBlockHandler.handle(transaction);

        assertTrue(client.getBlockedFor());
        assertNull(client.getBlockedTime());
        assertEquals(BLOCKED, account.getAccountStatus());
        verify(checkWebClient, never()).check(anyLong());
    }
}
