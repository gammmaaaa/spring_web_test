package ru.t1.java.demo.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.t1.java.demo.exception.TransactionException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<String> transactionException(TransactionException transactionException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(transactionException.getMessage());
    }
}
