package com.paymybud.backend.repository;

import com.paymybud.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("select t from Transaction t join t.sender s where s.id = " +
            ":id and t" +
            ".typeOfTransaction = 'TO_FRIEND' ")
    List<Transaction> getAllSentTransactionByUser(Integer id);

    @Query("select t from Transaction t join t.receiver r where r.id = " +
            ":id and t" +
            ".typeOfTransaction = 'TO_FRIEND' ")
    List<Transaction> getAllReceivedTransactionByUser(Integer id);
}
