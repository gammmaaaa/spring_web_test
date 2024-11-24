package ru.t1.java.demo.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.dto.AccountDTO;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.service.ClientService;
import ru.t1.java.demo.util.AccountMapperImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.t1.java.demo.model.enums.AccountStatusEnum.OPEN;
import static ru.t1.java.demo.model.enums.AccountTypeEnum.CREDIT;

@ExtendWith(MockitoExtension.class)
public class AccountMapperTest {

    @InjectMocks
    private AccountMapperImpl accountMapper;

    @Mock
    private ClientService clientService;

    private AccountDTO accountDTO;
    private Account account;
    private Client client;

    @BeforeEach
    public void setUp() {

        client = Client.builder()
                .build();

        account = Account.builder()
                .accountType(CREDIT)
                .accountStatus(OPEN)
                .balance(BigDecimal.valueOf(1000L))
                .client(client)
                .frozenAmount(BigDecimal.ZERO)
                .build();

        accountDTO = AccountDTO.builder()
                .id(1L)
                .accountType(CREDIT.name())
                .balance(BigDecimal.valueOf(1000L))
                .clientId(1L)
                .frozenAmount(BigDecimal.ZERO)
                .status(OPEN.name())
                .build();
    }

    @Test
    @DisplayName("Тестирование маппинга AccountDTO в Account")
    public void testToEntity() {
        when(clientService.getClientById(accountDTO.getClientId())).thenReturn(client);

        Account result = accountMapper.toEntity(accountDTO);

        assertNotNull(result);
        assertEquals(CREDIT, result.getAccountType());
        assertEquals(accountDTO.getBalance(), result.getBalance());
        assertEquals(client, result.getClient());
        verify(clientService).getClientById(accountDTO.getClientId());
    }

    @Test
    @DisplayName("Тестирование ошибки при маппинге AccountDTO в Account")
    public void testToEntity_InvalidAccountType() {
        accountDTO.setAccountType("INVALID_TYPE");

        Exception exception = assertThrows(NullPointerException.class, () -> {
            accountMapper.toEntity(accountDTO);
        });

        assertNotNull(exception);
    }

    @Test
    @DisplayName("Тестирование маппинга Account в AccountDTO")
    public void testToDTO() {
        AccountDTO result = accountMapper.toDTO(account);

        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        assertEquals(account.getAccountType().name(), result.getAccountType());
        assertEquals(account.getBalance(), result.getBalance());
        assertEquals(account.getClient().getId(), result.getClientId());
        assertEquals(account.getFrozenAmount(), result.getFrozenAmount());
        assertEquals(account.getAccountStatus().name(), result.getStatus());
    }
}
