package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.t1.java.demo.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select t from Transaction t where t.account.client.id = :clientId and t.timeTransaction > t.account.client.blockedTime")
    List<Transaction> findTransactionsAfterClientBlock(Long clientId);
}