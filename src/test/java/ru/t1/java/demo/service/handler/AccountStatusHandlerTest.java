package ru.t1.java.demo.service.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.dto.TransactionForAccept;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.mapper.TransactionMapper;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.AccountStatusEnum;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.impl.handler.impl.AccountStatusHandler;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.t1.java.demo.model.enums.AccountStatusEnum.OPEN;
import static ru.t1.java.demo.model.enums.TransactionStatusEnum.REQUESTED;

@ExtendWith(MockitoExtension.class)
public class AccountStatusHandlerTest {

    @InjectMocks
    private AccountStatusHandler accountStatusHandler;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private KafkaClientProducer kafkaClientProducer;

    @Mock
    private TransactionMapper transactionMapper;

    private Transaction transaction;
    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .balance(BigDecimal.valueOf(100L))
                .accountStatus(OPEN)
                .build();

        transaction = Transaction.builder()
                .account(account)
                .amount(BigDecimal.valueOf(50L))
                .build();
    }

    @Test
    void testHandle_AccountIsOpen_ShouldProcessTransaction() throws TransactionException {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toAccept(any(Transaction.class))).thenReturn(TransactionForAccept.builder().build());
        lenient().doNothing().when(kafkaClientProducer).sendTo(anyString(), any(TransactionForAccept.class));

        accountStatusHandler.handle(transaction);

        verify(transactionRepository).save(transaction);
        assertEquals(BigDecimal.valueOf(50L), account.getBalance());
        assertEquals(REQUESTED, transaction.getTransactionStatus());
    }

    @Test
    void testHandle_AccountIsClosed_ThrowTransactionException() {
        account.setAccountStatus(AccountStatusEnum.CLOSED);

        TransactionException exception = assertThrows(TransactionException.class, () -> {
            accountStatusHandler.handle(transaction);
        });

        assertEquals("Your account status is: CLOSED", exception.getMessage());
    }
}