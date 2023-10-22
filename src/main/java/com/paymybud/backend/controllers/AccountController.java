package com.paymybud.backend.controllers;


import com.paymybud.backend.entities.Account;
import com.paymybud.backend.service.AccountService;
import com.paymybud.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<Iterable<Account>> get() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.getAccount());
    }





}
