package com.paymybud.backend.service;

import com.paymybud.backend.dto.AccountDTO;
import com.paymybud.backend.entity.Account;
import com.paymybud.backend.entity.User;
import com.paymybud.backend.exceptions.BadRequestException;
import com.paymybud.backend.exceptions.NotFoundException;
import com.paymybud.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Iterable<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account add(AccountDTO accountDto) {
        Account account = new Account();
        account.setBalance(accountDto.balance());
        account.setIban(accountDto.iban());
        return account;
    }

    public void delete(Integer id) {



        accountRepository.deleteById(id);

    }

    public Account get(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unknown account"));
    }
    public void update(Account account, Double balance) {

        account.setBalance(balance);
        accountRepository.save(account);

    }


}
