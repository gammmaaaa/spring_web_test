package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.dto.TransactionDTO;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionMapperImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@LogDataSourceError
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapperImpl transactionMapper;

    @GetMapping(value = "/findAll")
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions()
                .stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/find/{id}")
    public TransactionDTO getTransactionById(@PathVariable long id) {
        return transactionMapper.toDTO(transactionService.getTransactionById(id));
    }

    @PostMapping(value = "/add")
    public TransactionDTO addNewTransaction(@RequestBody TransactionDTO transactionDTO) throws TransactionException {
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        return transactionMapper.toDTO(transactionService.saveTransaction(transaction));
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteAccount(@PathVariable long id) {
        transactionService.deleteTransactionById(id);

        return "Transaction with ID = " + id + " was deleted";
    }
}
