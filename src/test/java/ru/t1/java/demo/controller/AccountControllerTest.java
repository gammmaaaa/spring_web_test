package ru.t1.java.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.dto.AccountDTO;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.util.AccountMapperImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.t1.java.demo.model.enums.AccountTypeEnum.CREDIT;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountMapperImpl accountMapper;

    private Account account;
    private AccountDTO accountDTO;

    @BeforeEach
    public void setup() {
        account = Account.builder()
                .balance(BigDecimal.valueOf(1000L))
                .accountType(CREDIT)
                .build();
        accountDTO = AccountDTO.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(1000L))
                .accountType(CREDIT.name())
                .build();
    }

    @Test
    @DisplayName("Тестирование метода getAllAccounts для контроллера AccountController")
    public void testGetAllAccounts() {
        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(account));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(accountDTO);

        List<AccountDTO> result = accountController.getAllAccounts();

        assertEquals(1, result.size());
        assertEquals(accountDTO, result.get(0));
        verify(accountService).getAllAccounts();
        verify(accountMapper).toDTO(any(Account.class));
    }

    @Test
    @DisplayName("Тестирование метода getAccountById для контроллера AccountController")
    public void testGetAccountById() {
        when(accountService.getAccountById(1L)).thenReturn(account);
        when(accountMapper.toDTO(account)).thenReturn(accountDTO);

        AccountDTO result = accountController.getAccountById(1L);

        assertEquals(accountDTO, result);
        verify(accountService).getAccountById(1L);
        verify(accountMapper).toDTO(account);
    }

    @Test
    @DisplayName("Тестирование метода addNewAccount для контроллера AccountController")
    public void testAddNewAccount() {
        when(accountMapper.toEntity(accountDTO)).thenReturn(account);
        when(accountService.saveNewAccount(account)).thenReturn(account);
        when(accountMapper.toDTO(account)).thenReturn(accountDTO);

        AccountDTO result = accountController.addNewAccount(accountDTO);

        assertEquals(accountDTO, result);
        verify(accountMapper).toEntity(accountDTO);
        verify(accountService).saveNewAccount(account);
        verify(accountMapper).toDTO(account);
    }

    @Test
    @DisplayName("Тестирование метода updateAccount для контроллера AccountController")
    public void testUpdateAccount() {
        when(accountMapper.toEntity(accountDTO)).thenReturn(account);
        when(accountService.saveNewAccount(account)).thenReturn(account);
        when(accountMapper.toDTO(account)).thenReturn(accountDTO);

        AccountDTO result = accountController.updateAccount(accountDTO);

        assertEquals(accountDTO, result);
        verify(accountMapper).toEntity(accountDTO);
        verify(accountService).saveNewAccount(account);
        verify(accountMapper).toDTO(account);
    }

    @Test
    @DisplayName("Тестирование метода deleteAccount для контроллера AccountController")
    public void testDeleteAccount() {
        long accountId = 1L;

        String result = accountController.deleteAccount(accountId);

        assertEquals("Account with ID = 1 was deleted", result);
        verify(accountService).deleteAccountById(accountId);
    }
}