package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.t1.java.demo.model.Transaction;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId ORDER BY t.id DESC")
    Optional<Transaction> findTopAccount(@Param("accountId") Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.timestamp DESC")
    Optional<Transaction> findLatestTransaction(@Param("accountId") Long accountId);

}