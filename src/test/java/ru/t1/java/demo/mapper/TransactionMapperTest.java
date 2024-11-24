package ru.t1.java.demo.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.dto.TransactionDTO;
import ru.t1.java.demo.dto.TransactionForAccept;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.util.TransactionMapperImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.t1.java.demo.model.enums.TransactionStatusEnum.REQUESTED;

@ExtendWith(MockitoExtension.class)
public class TransactionMapperTest {

    @InjectMocks
    private TransactionMapperImpl transactionMapper;

    @Mock
    private AccountService accountService;

    private TransactionDTO transactionDTO;
    private Transaction transaction;
    private Account account;

    @BeforeEach
    public void setUp() {
        Client client = Client.builder()
                .build();
        account = Account.builder()
                .client(client)
                .balance(BigDecimal.valueOf(1000L))
                .build();

        transaction = Transaction.builder()
                .account(account)
                .amount(BigDecimal.valueOf(100L))
                .transactionStatus(REQUESTED)
                .build();

        transactionDTO = TransactionDTO.builder()
                .id(1L)
                .accountId(1L)
                .amount(BigDecimal.valueOf(100L))
                .status(REQUESTED.name())
                .build();
    }


    @Test
    @DisplayName("Тестирование маппинга TransactionDTO в Transaction")
    public void testToEntity() {
        when(accountService.getAccountById(transactionDTO.getAccountId())).thenReturn(account);

        Transaction result = transactionMapper.toEntity(transactionDTO);

        assertNotNull(result);
        assertEquals(account, result.getAccount());
        assertEquals(transactionDTO.getAmount(), result.getAmount());
        verify(accountService).getAccountById(transactionDTO.getAccountId());
    }

    @Test
    @DisplayName("Тестирование ошибки при маппинге TransactionDTO в Transaction")
    public void testToEntity_NullAmount() {
        transactionDTO.setAmount(null);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            transactionMapper.toEntity(transactionDTO);
        });

        assertNotNull(exception);
    }

    @Test
    @DisplayName("Тестирование маппинга Transaction в TransactionDTO")
    public void testToDTO() {
        TransactionDTO result = transactionMapper.toDTO(transaction);

        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAccount().getId(), result.getAccountId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getTransactionStatus().name(), result.getStatus());
    }

    @Test
    @DisplayName("Тестирование маппинга Transaction в TransactionForAccept")
    public void testToAccept() {
        TransactionForAccept result = transactionMapper.toAccept(transaction);

        assertNotNull(result);
        assertEquals(transaction.getAccount().getClient().getId(), result.getClientId());
        assertEquals(transaction.getAccount().getId(), result.getAccountId());
        assertEquals(transaction.getId(), result.getTransactionId());
        assertEquals(transaction.getTimeTransaction(), result.getTimeTransaction());
        assertEquals(transaction.getAmount(), result.getTransactionAmount());
        assertEquals(transaction.getAccount().getBalance(), result.getAccountBalance());
    }
}
