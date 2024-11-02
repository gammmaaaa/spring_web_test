package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.dto.AccountDTO;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.util.AccountMapper;

import java.util.List;
import java.util.stream.Collectors;

@LogDataSourceError
@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping(value = "/accounts")
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts()
                .stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/accounts/{id}")
    public AccountDTO getAccountById(@PathVariable long id) {
        return accountMapper.toDTO(accountService.getAccountById(id));
    }

    @LogDataSourceError
    @PostMapping(value = "/accounts")
    public AccountDTO addNewAccount(@RequestBody AccountDTO accountDTO) {
        Account account = accountMapper.toEntity(accountDTO);

        return accountMapper.toDTO(accountService.saveAccount(account));
    }

    @PutMapping(value = "/accounts")
    public AccountDTO updateAccount(@RequestBody AccountDTO accountDTO) {
        Account account = accountMapper.toEntity(accountDTO);

        return accountMapper.toDTO(accountService.saveAccount(account));
    }

    @DeleteMapping(value = "/accounts/{id}")
    public String deleteAccount(@PathVariable long id) {
        accountService.deleteAccountById(id);

        return "Account with ID = " + id + " was deleted";
    }
}
