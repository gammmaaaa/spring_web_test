package ru.t1.java.demo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.impl.TransactionServiceImpl;
import ru.t1.java.demo.service.impl.handler.TransactionHandler;
import ru.t1.java.demo.service.impl.handler.impl.TransactionHandlerChain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionHandlerChain transactionHandlerChain;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Тестирование метода getAllTransactions для сервиса TransactionService")
    void testGetAllTransactions() {
        Transaction transaction1 = Transaction.builder().build();
        Transaction transaction2 = Transaction.builder().build();
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getAllTransactions();

        assertEquals(2, result.size());
        verify(transactionRepository).findAll();
    }

    @Test
    @DisplayName("Тестирование метода getTransactionById для сервиса TransactionService")
    void testGetTransactionById() {
        long id = 1L;
        Transaction transaction = Transaction.builder().build();
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransactionById(id);

        assertNotNull(result);
        verify(transactionRepository).findById(id);
    }

    @Test
    @DisplayName("Тестирование метода saveTransaction для сервиса TransactionService")
    void testSaveTransaction() throws TransactionException {
        Transaction transaction = Transaction.builder().build();
        TransactionHandler mockHandler = mock(TransactionHandler.class);

        when(transactionHandlerChain.create()).thenReturn(mockHandler);
        doNothing().when(mockHandler).handle(transaction);

        Transaction result = transactionService.saveTransaction(transaction);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Тестирование метода updateTransaction для сервиса TransactionService")
    void testUpdateTransaction() {
        Transaction transaction = Transaction.builder().build();
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.updateTransaction(transaction);

        assertNotNull(result);
        verify(transactionRepository).save(transaction);
    }

    @Test
    @DisplayName("Тестирование метода deleteTransactionById для сервиса TransactionService")
    void testDeleteTransactionById() {
        long id = 1L;

        transactionService.deleteTransactionById(id);

        verify(transactionRepository).deleteById(id);
    }
}