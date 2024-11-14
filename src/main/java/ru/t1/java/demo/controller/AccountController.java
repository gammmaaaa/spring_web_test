package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.aop.Metric;
import ru.t1.java.demo.dto.AccountDTO;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.util.AccountMapperImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@LogDataSourceError
@Metric(time = 1000L)
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountMapperImpl accountMapper;

    @GetMapping(value = "/findAll")
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts()
                .stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/find/{id}")
    public AccountDTO getAccountById(@PathVariable long id) {
        return accountMapper.toDTO(accountService.getAccountById(id));
    }

    @PostMapping(value = "/add")
    public AccountDTO addNewAccount(@RequestBody AccountDTO accountDTO) {
        Account account = accountMapper.toEntity(accountDTO);

        return accountMapper.toDTO(accountService.saveNewAccount(account));
    }

    @PutMapping(value = "/update")
    public AccountDTO updateAccount(@RequestBody AccountDTO accountDTO) {
        Account account = accountMapper.toEntity(accountDTO);

        return accountMapper.toDTO(accountService.saveNewAccount(account));
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteAccount(@PathVariable long id) {
        accountService.deleteAccountById(id);

        return "Account with ID = " + id + " was deleted";
    }
}
