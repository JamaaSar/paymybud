package com.paymybud.backend.dto;


import com.paymybud.backend.entity.TypeOfTransaction;
import com.paymybud.backend.entity.User;

public record TransactionDTO(Double amount, String message,
                             TypeOfTransaction typeOfTransaction,
                             User sender,
                             User receiver){ }
