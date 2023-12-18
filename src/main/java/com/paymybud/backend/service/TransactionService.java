package com.paymybud.backend.service;

import com.paymybud.backend.dto.TransactionDTO;
import com.paymybud.backend.entity.Transaction;
import com.paymybud.backend.exceptions.NotFoundException;
import com.paymybud.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Iterable<Transaction> getAll() {
        return transactionRepository.findAll();

    }

    public void add(TransactionDTO transactionDto) {
        Transaction Transaction = new Transaction();
        Transaction.setTypeOfTransaction(transactionDto.typeOfTransaction());
        Transaction.setMessage(transactionDto.message());
        Transaction.setAmount(transactionDto.amount());
        Transaction.setReceiver(transactionDto.receiver());
        Transaction.setSender(transactionDto.sender());
        transactionRepository.save(Transaction);
    }

    public void delete(Integer id) {
        transactionRepository.deleteById(id);

    }


    public Transaction get(Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unknown transaction"));
    }

    public List<Transaction> getAllSentTransactionByUser(Integer id) {
        System.out.println("hiservice");
        return
                transactionRepository.getAllSentTransactionByUser(id);
    }

    public List<Transaction> getAllReceivedTransactionByUser(Integer id){
        return transactionRepository.getAllReceivedTransactionByUser(id);


    }
}
