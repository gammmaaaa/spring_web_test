package ru.t1.java.demo.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.impl.TransactionServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.t1.java.demo.model.enums.TransactionStatusEnum.ACCEPTED;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionServiceImpl transactionService;

    private static Transaction transaction1;
    private static Transaction transaction2;

    @BeforeAll
    public static void setup() {
        transaction1 = getTransaction();
        transaction1.setId(1L);
        transaction2 = getTransaction();
        transaction2.setId(2L);
    }

    @Test
    @DisplayName("JUnit test для getAllTransactions метода")
    void handleGetAllTransactions_ReturnValidList() throws Exception {
        //given
        var transactions = List.of(transaction1, transaction2);

        //when
        Mockito.when(transactionRepository.findAll()).thenReturn(transactions);
        List<Transaction> requestedTransactions = transactionService.getAllTransactions();

        //then
        assertThat(requestedTransactions).isNotNull();
        assertThat(requestedTransactions.size()).isEqualTo(2);
        assertEquals(transactions, requestedTransactions);
    }

    @Test
    @DisplayName("JUnit test для getAllTransactions метода")
    void handleGetOneTransaction_ReturnValidTransaction() throws Exception {
        //given
        var transactionOptional = Optional.of(transaction1);

        //when
        Mockito.when(transactionRepository.findById(1L)).thenReturn(transactionOptional);
        Transaction requestedTransaction = transactionService.getTransactionById(1);

        //then
        assertThat(requestedTransaction).isNotNull();
        assertEquals(requestedTransaction, transaction1);
    }

    private static Transaction getTransaction() {
        return Transaction.builder()
                .account(Account.builder()
                        .balance(BigDecimal.valueOf(1000L))
                        .build())
                .transactionStatus(ACCEPTED)
                .amount(BigDecimal.valueOf(1000L))
                .timeTransaction(LocalDateTime.of(
                        1900,
                        1,
                        1,
                        0,
                        0,
                        0
                ))
                .build();
    }
}
