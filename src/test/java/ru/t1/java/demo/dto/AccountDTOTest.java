package ru.t1.java.demo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static ru.t1.java.demo.model.enums.AccountStatusEnum.BLOCKED;
import static ru.t1.java.demo.model.enums.AccountStatusEnum.OPEN;
import static ru.t1.java.demo.model.enums.AccountTypeEnum.CREDIT;
import static ru.t1.java.demo.model.enums.AccountTypeEnum.DEBIT;

public class AccountDTOTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Тестирование билдера для AccountDTO")
    public void testAccountDTOBuilder() {
        AccountDTO accountDTO = AccountDTO.builder()
                .id(1L)
                .clientId(123L)
                .accountType(CREDIT.name())
                .balance(BigDecimal.valueOf(1000L))
                .frozenAmount(BigDecimal.valueOf(100L))
                .status(OPEN.name())
                .build();

        assertNotNull(accountDTO);
        assertEquals(1L, accountDTO.getId());
        assertEquals(123L, accountDTO.getClientId());
        assertEquals(CREDIT.name(), accountDTO.getAccountType());
        assertEquals(BigDecimal.valueOf(1000L), accountDTO.getBalance());
        assertEquals(BigDecimal.valueOf(100L), accountDTO.getFrozenAmount());
        assertEquals(OPEN.name(), accountDTO.getStatus());
    }

    @Test
    @DisplayName("Тестирование геттеров и сеттеров для AccountDTO")
    public void testSettersAndGetters() {
        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setId(1L);
        accountDTO.setClientId(123L);
        accountDTO.setAccountType(DEBIT.name());
        accountDTO.setBalance(BigDecimal.valueOf(500L));
        accountDTO.setFrozenAmount(BigDecimal.valueOf(50L));
        accountDTO.setStatus(BLOCKED.name());

        assertEquals(1L, accountDTO.getId());
        assertEquals(123L, accountDTO.getClientId());
        assertEquals(DEBIT.name(), accountDTO.getAccountType());
        assertEquals(BigDecimal.valueOf(500L), accountDTO.getBalance());
        assertEquals(BigDecimal.valueOf(50L), accountDTO.getFrozenAmount());
        assertEquals(BLOCKED.name(), accountDTO.getStatus());
    }

    @Test
    @DisplayName("Тестирование сериализации для AccountDTO")
    public void testJsonSerialization() throws JsonProcessingException {
        AccountDTO accountDTO = AccountDTO.builder()
                .id(1L)
                .clientId(123L)
                .accountType(CREDIT.name())
                .balance(BigDecimal.valueOf(1000L))
                .frozenAmount(BigDecimal.valueOf(100L))
                .status(OPEN.name())
                .build();

        String json = objectMapper.writeValueAsString(accountDTO);

        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"client_id\":123"));
        assertTrue(json.contains("\"type\":\"CREDIT\""));
        assertTrue(json.contains("\"balance\":1000"));
        assertTrue(json.contains("\"frozen_amount\":100"));
        assertTrue(json.contains("\"status\":\"OPEN\""));
    }

    @Test
    @DisplayName("Тестирование десериализации для AccountDTO")
    public void testJsonDeserialization() throws JsonProcessingException {
        String json = """
                {
                    "id": 1,
                    "client_id": 123,
                    "type": "CREDIT",
                    "balance": 1000,
                    "frozen_amount": 100,
                    "status": "OPEN"
                }""";

        AccountDTO accountDTO = objectMapper.readValue(json, AccountDTO.class);

        assertNotNull(accountDTO);
        assertEquals(1L, accountDTO.getId());
        assertEquals(123L, accountDTO.getClientId());
        assertEquals(CREDIT.name(), accountDTO.getAccountType());
        assertEquals(BigDecimal.valueOf(1000L), accountDTO.getBalance());
        assertEquals(BigDecimal.valueOf(100L), accountDTO.getFrozenAmount());
        assertEquals(OPEN.name(), accountDTO.getStatus());
    }
}
