package ru.t1.java.demo.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.t1.java.demo.model.enums.AccountStatusEnum.BLOCKED;
import static ru.t1.java.demo.model.enums.AccountStatusEnum.OPEN;
import static ru.t1.java.demo.model.enums.AccountTypeEnum.CREDIT;
import static ru.t1.java.demo.model.enums.AccountTypeEnum.DEBIT;

public class AccountTest {

    @Test
    @DisplayName("Тестирование билдера для сущности Account")
    public void testAccountBuilder() {
        Account account = Account.builder()
                .accountType(CREDIT)
                .accountStatus(OPEN)
                .balance(BigDecimal.valueOf(1000L))
                .frozenAmount(BigDecimal.valueOf(100L))
                .build();

        assertNotNull(account);
        assertEquals(CREDIT, account.getAccountType());
        assertEquals(OPEN, account.getAccountStatus());
        assertEquals(BigDecimal.valueOf(1000L), account.getBalance());
        assertEquals(BigDecimal.valueOf(100L), account.getFrozenAmount());
        assertNotNull(account.getTransactions());
        assertTrue(account.getTransactions().isEmpty());
    }

    @Test
    @DisplayName("Тестирование геттеров и сеттеров для сущности Account")
    public void testSettersAndGetters() {
        Account account = Account.builder().build();

        account.setAccountType(DEBIT);
        account.setAccountStatus(BLOCKED);
        account.setBalance(BigDecimal.valueOf(500L));
        account.setFrozenAmount(BigDecimal.valueOf(50L));

        assertEquals(DEBIT, account.getAccountType());
        assertEquals(BLOCKED, account.getAccountStatus());
        assertEquals(BigDecimal.valueOf(500L), account.getBalance());
        assertEquals(BigDecimal.valueOf(50L), account.getFrozenAmount());
    }

    @Test
    @DisplayName("Тестирование списка транзакций для сущности Account")
    public void testTransactionsList() {
        Account account = Account.builder().build();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(Transaction.builder().build());
        account.setTransactions(transactions);

        assertEquals(1, account.getTransactions().size());
    }

    @Test
    @DisplayName("Тестирование добавление сущности Client для сущности Account")
    public void testClientRelationship() {
        Account account = Account.builder().build();
        Client client = Client.builder().build();

        account.setClient(client);

        assertEquals(client, account.getClient());
    }

    @Test
    @DisplayName("Тестирование добавления Id для сущности Account")
    public void testIdMethods() {
        Account account = Account.builder().build();

        Long expectedId = 1L;
        account.setId(expectedId);

        assertEquals(expectedId, account.getId());
    }
}
