package ru.t1.java.demo.service.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.impl.handler.impl.BlockedClientHandler;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.t1.java.demo.model.enums.AccountStatusEnum.ARRESTED;
import static ru.t1.java.demo.model.enums.TransactionStatusEnum.REJECTED;

@ExtendWith(MockitoExtension.class)
public class BlockedClientHandlerTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BlockedClientHandler blockedClientHandler;

    private Transaction transaction;
    private Account account;
    private Client client;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        client = Client.builder().build();
        account = Account.builder()
                .client(client)
                .build();
        transaction = Transaction.builder()
                .account(account)
                .build();

        Field field = BlockedClientHandler.class.getDeclaredField("countTransactionsRejected");
        field.setAccessible(true);
        field.set(blockedClientHandler, 3L);
    }

    @Test
    void testHandle_ClientIsBlocked_ExceedsRejectedCount() throws TransactionException {
        client.setBlockedFor(true);
        client.setId(1L);
        when(transactionRepository.findTransactionsAfterClientBlock(client.getId()))
                .thenReturn(Arrays.asList(Transaction.builder().build(), Transaction.builder().build()));

        blockedClientHandler.handle(transaction);

        assertEquals(ARRESTED, account.getAccountStatus());
        assertEquals(REJECTED, transaction.getTransactionStatus());
        assertNotNull(transaction.getTimeTransaction());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void testHandle_ClientIsBlocked_DoesNotExceedRejectedCount() throws TransactionException {
        client.setBlockedFor(true);
        client.setId(1L);
        when(transactionRepository.findTransactionsAfterClientBlock(client.getId()))
                .thenReturn(Arrays.asList(Transaction.builder().build()));

        blockedClientHandler.handle(transaction);

        assertNotEquals(ARRESTED, account.getAccountStatus());
        assertEquals(REJECTED, transaction.getTransactionStatus());
        assertNotNull(transaction.getTimeTransaction());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void testHandle_ClientIsNotBlocked() throws TransactionException {
        client.setBlockedFor(false);

        blockedClientHandler.handle(transaction);

        assertNotEquals(ARRESTED, account.getAccountStatus());
        verify(transactionRepository, never()).findTransactionsAfterClientBlock(anyLong());
        verify(transactionRepository, never()).save(any());
    }
}
