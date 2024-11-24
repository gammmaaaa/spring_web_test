package ru.t1.java.demo.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.t1.java.demo.model.enums.TransactionStatusEnum.ACCEPTED;
import static ru.t1.java.demo.model.enums.TransactionStatusEnum.REJECTED;

public class TransactionTest {

    @Test
    @DisplayName("Тестирование билдера для сущности Transaction")
    public void testTransactionBuilder() {
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(100L))
                .transactionStatus(ACCEPTED)
                .timeTransaction(LocalDateTime.now())
                .build();

        assertNotNull(transaction);
        assertEquals(BigDecimal.valueOf(100L), transaction.getAmount());
        assertEquals(ACCEPTED, transaction.getTransactionStatus());
        assertNotNull(transaction.getTimeTransaction());
    }

    @Test
    @DisplayName("Тестирование геттеров и сеттеров для сущности Transaction")
    public void testSettersAndGetters() {
        Transaction transaction = Transaction.builder().build();

        transaction.setAmount(BigDecimal.valueOf(200L));
        transaction.setTransactionStatus(REJECTED);
        transaction.setTimeTransaction(LocalDateTime.now());

        assertEquals(BigDecimal.valueOf(200L), transaction.getAmount());
        assertEquals(REJECTED, transaction.getTransactionStatus());
        assertNotNull(transaction.getTimeTransaction());
    }

    @Test
    @DisplayName("Тестирование добавления Id для сущности Transaction")
    public void testIdMethods() {
        Transaction transaction = Transaction.builder().build();

        Long expectedId = 1L;
        transaction.setId(expectedId);

        assertEquals(expectedId, transaction.getId());
    }
}
