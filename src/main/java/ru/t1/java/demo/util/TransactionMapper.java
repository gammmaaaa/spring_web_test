package ru.t1.java.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionDTO;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.AccountService;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final AccountService accountService;

    public Transaction toEntity(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .account(accountService.getAccountById(transactionDTO.getAccountId()))
                .amount(transactionDTO.getAmount())
                .build();
    }

    public TransactionDTO toDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .amount(transaction.getAmount())
                .timeTransaction(transaction.getTimeTransaction())
                .build();
    }
}
