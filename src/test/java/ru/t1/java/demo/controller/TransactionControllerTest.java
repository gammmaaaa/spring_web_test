package ru.t1.java.demo.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1.java.demo.dto.TransactionDTO;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionMapperImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.t1.java.demo.model.enums.TransactionStatusEnum.ACCEPTED;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    TransactionService transactionService;

    @MockBean
    TransactionMapperImpl transactionMapperImpl;

    private static TransactionDTO transactionDTO1;
    private static TransactionDTO transactionDTO2;
    private static Transaction transaction1;
    private static Transaction transaction2;

    @BeforeAll
    public static void setup() {
        transaction1 = getTransaction();
        transaction2 = getTransaction();
        transactionDTO1 = getTransactionDTO(1L, 1L);
        transactionDTO2 = getTransactionDTO(2L, 2L);
    }

    @Test
    @DisplayName("GET /transactions/findAll возвращает HTTP-ответ со статусом 200 OK и списком транзакций")
    void handleGetAllTransactions_ReturnValidList() throws Exception {
        //given
        var requestBuilder = get("/transactions/findAll");

        var transactionsDTO = List.of(transactionDTO1, transactionDTO2);
        var transactions = List.of(transaction1, transaction2);

        //when
        Mockito.when(transactionMapperImpl.toDTO(transactions.get(0))).thenReturn(transactionsDTO.get(0));
        Mockito.when(transactionMapperImpl.toDTO(transactions.get(1))).thenReturn(transactionsDTO.get(1));
        Mockito.when(transactionService.getAllTransactions())
                .thenReturn(transactions);

        mvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id": 1,
                                        "account_id": 1,
                                        "amount": 1000,
                                        "status": "ACCEPTED",
                                        "time_transaction": "1900-01-01T00:00:00"
                                    },
                                    {
                                        "id": 2,
                                        "account_id": 2,
                                        "amount": 1000,
                                        "status": "ACCEPTED",
                                        "time_transaction": "1900-01-01T00:00:00"
                                    }
                                ]
                                """)
                );
    }

    @Test
    @DisplayName("GET /transactions/find/{id} возвращает HTTP-ответ со статусом 200 OK и транзакцию")
    void handleGetOneTransaction_ReturnValidTransaction() throws Exception {
        //given
        var requestBuilder = get("/transactions/find/1");

        var transactionDTO = transactionDTO1;
        var transaction = transaction1;

        //when
        Mockito.when(transactionMapperImpl.toDTO(transaction)).thenReturn(transactionDTO);
        Mockito.when(transactionService.getTransactionById(1))
                .thenReturn(transaction);

        mvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                        {
                                            "id": 1,
                                            "account_id": 1,
                                            "amount": 1000,
                                            "status": "ACCEPTED",
                                            "time_transaction": "1900-01-01T00:00:00"
                                        }
                                """)
                );
    }

    @Test
    @DisplayName("POST /transactions/add возвращает HTTP-ответ со статусом 200 OK и транзакцией")
    void handleAddNewTransaction_ReturnValidTransaction() throws Exception {
        //given
        var requestBuilder = post("/transactions/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "amount": 1000,
                            "account_id": 1
                        }
                        """);

        var transactionDTO = transactionDTO1;
        var transaction = transaction1;

        //when
        Mockito.when(transactionMapperImpl.toDTO(transaction)).thenReturn(transactionDTO);
        Mockito.when(transactionService.saveTransaction(Mockito.any()))
                .thenReturn(transaction);

        mvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                        {
                                            "id": 1,
                                            "account_id": 1,
                                            "amount": 1000,
                                            "status": "ACCEPTED",
                                            "time_transaction": "1900-01-01T00:00:00"
                                        }
                                """)
                );
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

    private static TransactionDTO getTransactionDTO(Long id, Long accountId) {
        return TransactionDTO.builder()
                .id(id)
                .accountId(accountId)
                .amount(BigDecimal.valueOf(1000L))
                .status(ACCEPTED.name())
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
