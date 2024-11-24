package ru.t1.java.demo.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.t1.java.demo.model.enums.TransactionStatusEnum.ACCEPTED;

public class TransactionDTOTest {

    private static TransactionDTO transactionDTO1;
    private static TransactionDTO transactionDTO2;
    private static TransactionDTO transactionDTO3;

    @BeforeAll
    public static void setup() {
        transactionDTO1 = getTransactionDTO(1L);
        transactionDTO2 = getTransactionDTO(1L);
        transactionDTO3 = getTransactionDTO(2L);
        ;
    }

    @Test
    @DisplayName("Test TransactionDTO")
    void handleTransactionDTO() {
        assertThat(transactionDTO1.getId()).isNull();
        assertEquals(transactionDTO1.getAccountId(), 1L);
        assertEquals(transactionDTO1.getAmount(), BigDecimal.valueOf(1000L));
        assertThat(transactionDTO1.getTimeTransaction()).isNotNull();
        assertEquals(transactionDTO1.getStatus(), "ACCEPTED");
        assertEquals(transactionDTO1, transactionDTO2);
        assertNotEquals(transactionDTO1, transactionDTO3);
    }


    private static TransactionDTO getTransactionDTO(Long accountId) {
        return TransactionDTO.builder()
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
