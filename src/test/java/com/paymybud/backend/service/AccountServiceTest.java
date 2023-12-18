package com.paymybud.backend.service;


import com.paymybud.backend.dto.*;
import com.paymybud.backend.entity.Account;
import com.paymybud.backend.exceptions.NotFoundException;
import com.paymybud.backend.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;

    private Account account;
    private Account friendsAccount;
    private AccountDTO accountDto;
    private AccountDTO friendsAccountDTO;


    @BeforeEach
    public void setUp() {

        account = new Account();
        account.setBalance(1000.0);
        account.setIban("test123");

        accountDto = new AccountDTO(1000.0, "test123");

        friendsAccount = new Account();
        friendsAccount.setBalance(1000.0);
        friendsAccount.setIban("test1234");

        friendsAccountDTO = new AccountDTO(1000.0, "test1234");

    }

    @Test
    @DisplayName("Add OK test")
    public void testAdd() {

        // Given.

        // When.
        Account account1 = accountService.add(accountDto);
        // Then.
        assertNotNull(account1);    }

    @Test
    @DisplayName("Get OK test")
    public void testGet() {

        // Given.

        // When.
        when(accountRepository.findById(1)).thenReturn(Optional.ofNullable(account));
        Account account1 = accountService.get(1);
        // Then.
        assertEquals(account.getBalance(), account1.getBalance());
        assertEquals(account.getIban(), account1.getIban());

    }


    @Test
    @DisplayName("Get KO test")
    public void testGetKO() {

        // Given.

        // When.

        // Then.
        assertThrows(NotFoundException.class,
                () ->  accountService.get(2));

    }
    @Test
    @DisplayName("GetAll OK test")
    public void testGetAll() {

        // Given.
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);

        // When.
        when(accountRepository.findAll()).thenReturn(accountList);
        Iterable<Account> res = accountService.getAll();
        // Then.

        int counter = 0;
        for (Object i : res) {
            counter++;
        }
        // Then.
        assertEquals(1, counter);

    }
    @Test
    @DisplayName("Get OK test")
    public void testUpdate() {

        // Given.

        // When.
        accountService.update(account,10.0);
        // Then.
        verify(accountRepository, times(1)).save(account);
    }
    @Test
    @DisplayName("Get OK test")
    public void testDelete() {

        // Given.

        // When.
        accountService.delete(1);
        // Then.
        verify(accountRepository, times(1)).deleteById(1);
    }

}
