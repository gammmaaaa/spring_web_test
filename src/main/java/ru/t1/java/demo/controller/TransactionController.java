package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.dto.TransactionDTO;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping(value = "/transactions")
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions()
                .stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/transactions/{id}")
    public TransactionDTO getTransactionById(@PathVariable long id) {
        return transactionMapper.toDTO(transactionService.getTransactionById(id));
    }

    @PostMapping(value = "/transactions")
    public String addNewTransaction(@RequestBody TransactionDTO transactionDTO) {
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        return transactionService.saveTransaction(transaction);
    }

    @DeleteMapping(value = "/transactions/{id}")
    public String deleteAccount(@PathVariable long id) {
        transactionService.deleteTransactionById(id);

        return "Transaction with ID = " + id + " was deleted";
    }
}
