package ru.t1.java.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.AccountDTO;
import ru.t1.java.demo.mapper.AccountMapper;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.enums.AccountTypeEnum;
import ru.t1.java.demo.service.ClientService;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AccountMapperImpl implements AccountMapper {
    private final ClientService clientService;

    @Override
    public Account toEntity(AccountDTO accountDTO) {
        if (Arrays.stream(AccountTypeEnum.values()).toList().stream().noneMatch(type -> type.name().equals(accountDTO.getAccountType()))) {
            throw new NullPointerException();
        }
        return Account.builder()
                .accountType(AccountTypeEnum.valueOf(accountDTO.getAccountType()))
                .balance(accountDTO.getBalance())
                .client(clientService.getClientById(accountDTO.getClientId()))
                .build();
    }

    @Override
    public AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountType(account.getAccountType().name())
                .balance(account.getBalance())
                .clientId(account.getClient().getId())
                .build();
    }
}
