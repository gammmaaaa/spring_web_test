package ru.t1.java.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.AccountDTO;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.enums.AccountTypeEnum;
import ru.t1.java.demo.service.ClientService;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final ClientService clientService;

    public Account toEntity(AccountDTO accountDTO) {
        return Account.builder()
                .accountType(AccountTypeEnum.valueOf(accountDTO.getAccountType()))
                .balance(accountDTO.getBalance())
                .client(clientService.getClientById(accountDTO.getClientId()))
                .build();
    }

    public AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountType(account.getAccountType().name())
                .balance(account.getBalance())
                .clientId(account.getClient().getId())
                .build();
    }
}
