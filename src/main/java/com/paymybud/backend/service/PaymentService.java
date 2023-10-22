package com.paymybud.backend.service;

import com.paymybud.backend.entities.Account;
import com.paymybud.backend.entities.TypeOfPayment;
import com.paymybud.backend.repository.AccountRepository;
import com.paymybud.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;




    public void makePayment(Account account, TypeOfPayment type) {

    }

}
