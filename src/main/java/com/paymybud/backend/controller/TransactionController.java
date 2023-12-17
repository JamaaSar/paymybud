package com.paymybud.backend.controller;


import com.paymybud.backend.entity.Transaction;
import com.paymybud.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/sent/{id}")
    public ResponseEntity<Iterable<Transaction>> getTransfertByUserId(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.getAllSentTransactionByUser(id));
    }
    @GetMapping("/received/{id}")
    public ResponseEntity<Iterable<Transaction>> getAllReceivedByUser(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.getAllReceivedTransactionByUser(id));
    }





}
