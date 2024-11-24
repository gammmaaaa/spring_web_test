package ru.t1.java.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.service.impl.AccountServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.t1.java.demo.model.enums.AccountStatusEnum.OPEN;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    public void setUp() {
        account = Account.builder()
                .accountStatus(OPEN)
                .frozenAmount(BigDecimal.ZERO)
                .build();

        account.setId(1L);
    }

    @Test
    @DisplayName("Тестирование метода getAllAccounts для сервиса AccountService")
    public void testGetAllAccounts() {
        Account account2 = Account.builder().build();
        account2.setId(2L);
        List<Account> accounts = Arrays.asList(account, account2);

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountRepository).findAll();
    }

    @Test
    @DisplayName("Тестирование метода getAllAccounts для сервиса AccountService при условии его существования")
    public void testGetAccountById_Exists() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountById(1L);

        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        verify(accountRepository).findById(1L);
    }

    @Test
    @DisplayName("Тестирование метода getAllAccounts для сервиса AccountService при условии его отсутствия")
    public void testGetAccountById_NotExists() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Account result = accountService.getAccountById(1L);

        assertNull(result);
        verify(accountRepository).findById(1L);
    }

    @Test
    @DisplayName("Тестирование метода saveNewAccount для сервиса AccountService")
    public void testSaveNewAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.saveNewAccount(account);

        assertNotNull(result);
        assertEquals(OPEN, result.getAccountStatus());
        assertEquals(BigDecimal.ZERO, result.getFrozenAmount());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Тестирование метода updateAccount для сервиса AccountService")
    public void testUpdateAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.updateAccount(account);

        assertNotNull(result);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Тестирование метода deleteAccount для сервиса AccountService")
    public void testDeleteAccountById() {
        doNothing().when(accountRepository).deleteById(1L);

        accountService.deleteAccountById(1L);

        verify(accountRepository).deleteById(1L);
    }
}
