package ru.t1.java.demo.service;

import ru.t1.java.demo.model.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();

    Account getAccountById(long id);

    Account saveAccount(Account account);

    void deleteAccountById(long id);
}
