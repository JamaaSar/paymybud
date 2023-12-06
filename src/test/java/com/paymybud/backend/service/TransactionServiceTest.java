package com.paymybud.backend.service;


import com.paymybud.backend.dto.AccountDTO;
import com.paymybud.backend.dto.TransactionDTO;
import com.paymybud.backend.entity.Account;
import com.paymybud.backend.entity.Transaction;
import com.paymybud.backend.entity.TypeOfTransaction;
import com.paymybud.backend.entity.User;
import com.paymybud.backend.exceptions.NotFoundException;
import com.paymybud.backend.repository.TransactionRepository;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;
    private Account account;
    private Account friendsAccount;
    private AccountDTO accountDto;
    private AccountDTO friendsAccountDTO;
    private TransactionDTO transactionDto;
    private Transaction transaction = new Transaction();
    private User testUser = new User();
    private User testFriend = new User();

    @BeforeEach
    public void setUp() {

        testUser.setId(1);
        testUser.setFirstname("testPersonFirstName");
        testUser.setLastname("testPersonLastName");
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("test123");

        testFriend.setId(2);
        testFriend.setFirstname("testFriendFirstName");
        testFriend.setLastname("testFriendLastName");
        testFriend.setEmail("testFriend@gmail.com");
        testFriend.setPassword("test123");


        account = new Account();
        account.setId(1);
        account.setBalance(1000);
        account.setIban("test123");
        testUser.setAccount(account);
        accountDto = new AccountDTO(1000, "test123");

        friendsAccount = new Account();
        friendsAccount.setId(2);
        friendsAccount.setBalance(1000);
        friendsAccount.setIban("test1234");
        testFriend.setAccount(friendsAccount);
        friendsAccountDTO = new AccountDTO(1000, "test1234");
        transaction.setReceiver(testFriend);
        transaction.setSender(testUser);
        transaction.setAmount(10);
        transaction.setMessage(null);
        transaction.setTypeOfTransaction(TypeOfTransaction.TO_FRIEND);

        transactionDto =  new TransactionDTO(10, null,
                TypeOfTransaction.TO_FRIEND, testUser, testFriend);

    }

    @Test
    @DisplayName("Add OK test")
    public void testAdd() {

        // Given.

        // When.
        transactionService.add(transactionDto);
        // Then.
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    @DisplayName("Get OK test")
    public void testGet() {

        // Given.

        // When.
        when(transactionRepository.findById(1)).thenReturn(Optional.ofNullable(
                transaction));
        Transaction transfert1 = transactionService.get(1);
        // Then.
        assertEquals(transaction.getAmount(), transfert1.getAmount());
        assertEquals(transaction.getMessage(), transfert1.getMessage());
        assertEquals(transaction.getReceiver(), transfert1.getReceiver());
        assertEquals(transaction.getSender(), transfert1.getSender());
        assertEquals(transaction.getTypeOfTransaction(), transfert1.getTypeOfTransaction());

    }
    @Test
    @DisplayName("GetAll OK test")
    public void testGetAll() {

        // Given.
        List<Transaction> transfertList = new ArrayList<>();
        transfertList.add(transaction);

        // When.
        when(transactionRepository.findAll()).thenReturn(transfertList);
        Iterable<Transaction> res = transactionService.getAll();
        // Then.

        int counter = 0;
        for (Object i : res) {
            counter++;
        }
        // Then.
        assertEquals(1, counter);

    }
    @Test
    @DisplayName("Get KO test")
    public void testGetKO() {

        // Given.

        // When.

        // Then.
        assertThrows(NotFoundException.class,
                () ->  transactionService.get(2));

    }
    @Test
    @DisplayName("GetAll OK test")
    public void testGetTrandfertByUserId() {

        // Given.
        List<Transaction> transfertList = new ArrayList<>();
        transfertList.add(transaction);

        // When.
        when(transactionRepository.getAllSentTransactionByUser(1)).thenReturn(transfertList);
        Iterable<Transaction> res = transactionService.getAllSentTransactionByUser(1);
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
    public void testDelete() {

        // Given.

        // When.
        transactionService.delete(1);
        // Then.
        verify(transactionRepository, times(1)).deleteById(1);
    }

}
