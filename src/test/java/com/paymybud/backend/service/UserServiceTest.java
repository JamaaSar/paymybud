package com.paymybud.backend.service;



import com.paymybud.backend.dto.*;
import com.paymybud.backend.entity.Account;
import com.paymybud.backend.entity.TypeOfTransaction;
import com.paymybud.backend.entity.User;
import com.paymybud.backend.exceptions.BadRequestException;
import com.paymybud.backend.mappers.UserMapper;
import com.paymybud.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private AccountService accountService;
    @Mock
    private  UserMapper userMapper;
    @Mock
    private  PasswordEncoder passwordEncoder;
    private User testUser = new User();
    private User testFriend = new User();
    private UserDTO testUserDTO;
    private SignUpDTO signUpDto ;
    private CredentialsDTO credentialsDto ;
    private Account account;
    private Account friendsAccount;
    private AccountDTO accountDto;
    private AccountDTO friendsAccountDTO;


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

        testUserDTO = new UserDTO();
        testUserDTO.setFirstname("testPersonFirstName");
        testUserDTO.setLastname("testPersonLastName");
        testUserDTO.setEmail("test@gmail.com");

        credentialsDto = new CredentialsDTO(testUser.getEmail(),
                testUser.getPassword());

        account = new Account();
        account.setBalance(1000);
        account.setIban("test123");

        accountDto = new AccountDTO(1000, "test123");

        friendsAccount = new Account();
        friendsAccount.setBalance(1000);
        friendsAccount.setIban("test1234");

        friendsAccountDTO = new AccountDTO(1000, "test1234");

        signUpDto  = new SignUpDTO(testUser.getFirstname(), testUser.getLastname(),
                testUser.getEmail(), testUser.getPassword());

    }

    @Test
    @DisplayName("Login OK test")
    public void testLogin() {

        // Given.

        // When.
        when(userRepository.findByEmail(credentialsDto.email())).thenReturn(
                Optional.ofNullable(testUser));
        when(userMapper.toUserDto(testUser)).thenReturn(testUserDTO);
        when(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
                testUser.getPassword())).thenReturn(true);
        UserDTO loggedUserDTO = userService.login(credentialsDto);

        // Then.
        assertEquals(testUserDTO.getEmail(), loggedUserDTO.getEmail());
        assertEquals(testUserDTO.getFirstname(), loggedUserDTO.getFirstname());
        assertEquals(testUserDTO.getLastname(), loggedUserDTO.getLastname());


    }
    @Test
    @DisplayName("Login KO pass incorrect test")
    public void testLoginPasswordKO() {

        // Given.

        // When.
        when(userRepository.findByEmail(credentialsDto.email())).thenReturn(
                Optional.ofNullable(testUser));
        when(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
                testUser.getPassword())).thenReturn(false);
        // Then.
        assertThrows(Exception.class,
                () -> userService.login(credentialsDto));

    }
    @Test
    @DisplayName("Login KO user doesnt exist test")
    public void testLoginUserKO() {

        // Given.

        // When.
        when(userRepository.findByEmail(credentialsDto.email())).thenReturn(
                Optional.ofNullable(null));
        // Then.
        assertThrows(Exception.class,
                () -> userService.login(credentialsDto));

    }
    @Test
    @DisplayName("Register OK test")
    public void testRegister() {

        // Given.
        // When.
        when(userRepository.findByEmail(credentialsDto.email())).thenReturn(
                Optional.ofNullable(null));
        when(userMapper.signUpToUser(signUpDto)).thenReturn(testUser);
        testUser.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toUserDto(testUser)).thenReturn(testUserDTO);

        UserDTO loggedUserDTO = userService.signup(signUpDto);

        // Then.
        assertEquals(testUserDTO.getEmail(), loggedUserDTO.getEmail());
        assertEquals(testUserDTO.getFirstname(), loggedUserDTO.getFirstname());
        assertEquals(testUserDTO.getLastname(), loggedUserDTO.getLastname());


    }
    @Test
    @DisplayName("Register KO test")
    public void testRegisterKO() {

        // Given.

        // When.
        when(userRepository.findByEmail(credentialsDto.email())).thenReturn(
                Optional.ofNullable(testUser));

        // Then.
        assertThrows(Exception.class,
                () -> userService.signup(signUpDto));

    }

    @Test
    @DisplayName("Account OK test")
    public void testAccountOK() {

        // Given.

        // When.
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(testUser));
        testUser.setAccount(account);
        userService.addAccount(1, accountDto);

        // Then.
        verify(userRepository, times(1)).save(testUser);


    }
    @Test
    @DisplayName("Account KO test")
    public void testAccountKO() {

        // Given.
        AccountDTO account1 = new AccountDTO(0 , "");
        // When.
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(testUser));
        // Then.
        assertThrows(Exception.class,
                () -> userService.addAccount(1, account1));

    }
    @Test
    @DisplayName("AddFriend OK test")
    public void testAddFriendOK() throws Exception {

        // Given.

        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.findByEmail("testFriend@gmail.com")).thenReturn(
                Optional.ofNullable(testFriend));
        userService.addFriend(1,"testFriend@gmail.com" );
        // Then.
        verify(userRepository, times(1)).save(testUser);


    }

    @Test
    @DisplayName("AddFriend KO deja dans le liste test")
    public void testAddFriendKODejaDansListe() throws Exception {

        // Given.
        List<User> friendsList = testUser.getFriendsList();
        friendsList.add(testFriend);
        testUser.setFriendsList(friendsList);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.findByEmail("testFriend@gmail.com")).thenReturn(
                Optional.ofNullable(testFriend));
        // Then.
        assertThrows(BadRequestException.class,
                () ->  userService.addFriend(1,"testFriend@gmail.com" ));

    }
    @Test
    @DisplayName("AddFriend KO test")
    public void testAddFriendKO() {

        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(
                Optional.ofNullable(testFriend));
        // Then.
        assertThrows(Exception.class,
                () ->  userService.addFriend(1,"test@gmail.com" ));


    }
    @Test
    @DisplayName("AddFriend friend KO test")
    public void testAddFriendDoesntFoundKO() {

        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.findByEmail("test12@gmail.com")).thenReturn(
                Optional.ofNullable(null));
        // Then.
        assertThrows(Exception.class,
                () ->  userService.addFriend(1,"test12@gmail.com" ));
    }

    @DisplayName("ChargerAccount KO test")
    public void testChargerAccountKO() throws Exception {

        // Given.
        testUser.setAccount(account);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        // Then.
        assertThrows(Exception.class,
                () ->    userService.chargerAccount(1, 10000, "charger"));
    }

    @DisplayName("ChargerAccount OK test")
    public void testChargerAccountOK() throws Exception {
        // Given.
        testUser.setAccount(account);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        doNothing().when(transactionService).add(new TransactionDTO(10, null,
                TypeOfTransaction.TO_OWN_CART, testUser, testUser));
        userService.chargerAccount(1, 10, "charger");
        // Then.
//        verify(userRepository, times(1)).save(testUser);
    }
    @Test
    @DisplayName("ChargerAccount OK test")
    public void testChargerAccountNotTransfertOK() throws Exception {
        // Given.
        testUser.setAccount(account);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        userService.chargerAccount(1, 10, "tirer");
        // Then.
      //  verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("ChargerAccount iban null KO test")
    public void testChargerAccountIbanKO() throws Exception {

        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        // Then.
        assertThrows(Exception.class,
                () ->  userService.chargerAccount(1, 10, "TEST"));
    }

    @DisplayName("ChargerAccount solde KO test")
    public void testChargerAccountSoldeKO() throws Exception {

        // Given.
        testUser.setAccount(account);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        // Then.
        assertThrows(Exception.class,
                () ->  userService.chargerAccount(1, 10000, "charger"));
    }

    @Test
    @DisplayName("FindByEmail KO test")
    public void testFindByEmailKO() throws Exception {

        // Given.

        // When.
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(
                Optional.ofNullable(null));
        // Then.
        assertThrows(Exception.class,
                () ->  userService.findByEmail("test@gmail.com"));
    }

    @Test
    @DisplayName("FindByEmail OK test")
    public void testFindByEmailOK() throws Exception {

        // Given.

        // When.
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(
                Optional.ofNullable(testUser));
        when(userMapper.toUserDto(testUser)).thenReturn(testUserDTO);
        UserDTO user1 = userService.findByEmail("test@gmail.com");
        // Then.
        assertEquals(testUserDTO.getEmail(), user1.getEmail());
        assertEquals(testUserDTO.getFirstname(), user1.getFirstname());
        assertEquals(testUserDTO.getLastname(), user1.getLastname());
    }

    @Test
    @DisplayName("FindById OK test")
    public void testFindByIdOK() throws Exception {

        // Given.

        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userMapper.toUserDto(testUser)).thenReturn(testUserDTO);
        UserDTO user1 = userService.findById(1);

        // Then.
        assertEquals(testUserDTO.getEmail(), user1.getEmail());
        assertEquals(testUserDTO.getFirstname(), user1.getFirstname());
        assertEquals(testUserDTO.getLastname(), user1.getLastname());


    }
    @Test
    @DisplayName("FindById KO test")
    public void testFindByIdKO() throws Exception {

        // Given.

        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(null));

        // Then.
        assertThrows(Exception.class,
                () ->  userService.findById(1));
    }

    @Test
    @DisplayName("FindById OK test")
    public void testGetAllUserExceptFriendsOK() throws Exception {

        // Given.
        User user1 = new User();
        user1.setFirstname("userFirstName");
        user1.setLastname("userLastName");
        user1.setEmail("userTest@gmail.com");
        user1.setPassword("test123");
        List<User> userList = new ArrayList<>();
        userList.add(user1);

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setFirstname("userFirstName");
        userDTO1.setLastname("userLastName");
        userDTO1.setEmail("userTest@gmail.com");
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO1);

        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.getAllUserExceptCurrentUser(1)).thenReturn(
                userList);
        when(userRepository.getAllUserExceptCurrentUser(1)).thenReturn(
                userList);
        when(userMapper.toUserDtoList(userList)).thenReturn(
                userDTOList);
        Iterable<UserDTO> res = userService.getAllUserExceptFriends(1);
        int counter = 0;
        for (Object i : res) {
            counter++;
        }
        // Then.
        assertEquals(1, counter);
    }
    @Test
    @DisplayName("FindById OK test")
    public void testGetAllUserExceptFriendsFilterOK() throws Exception {

        // Given.
        User user1 = new User();
        user1.setId(2);
        user1.setFirstname("userFirstName");
        user1.setLastname("userLastName");
        user1.setEmail("userTest@gmail.com");
        user1.setPassword("test123");

        User user2 = new User();
        user2.setId(3);
        user2.setFirstname("userFirstName1");
        user2.setLastname("userLastName1");
        user2.setEmail("userTest1@gmail.com");
        user2.setPassword("test123");

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        List<User> friendlist = testUser.getFriendsList();
        friendlist.add(user2);

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setFirstname("userFirstName");
        userDTO1.setLastname("userLastName");
        userDTO1.setEmail("userTest@gmail.com");
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO1);

        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.getAllUserExceptCurrentUser(1)).thenReturn(
                userList);
        userList.remove(user2);
        when(userMapper.toUserDtoList(userList)).thenReturn(
                userDTOList);
        Iterable<UserDTO> res = userService.getAllUserExceptFriends(1);
        int counter = 0;
        for (Object i : res) {
            counter++;
        }
        // Then.
        assertEquals(1, counter);
    }

    @Test
    @DisplayName("FindById KO test")
    public void testGetAllUserExceptFriendsKO() throws Exception {

        // Given.

        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(null));
        // Then.
        assertThrows(Exception.class,
                () ->  userService.findById(1));
    }

    @Test
    @DisplayName("EnvoyerArgentVersAmi OK test")
    public void testEnvoyerArgentVersAmiOK() throws Exception {
        // Given.
        testUser.setAccount(account);
        testFriend.setAccount(friendsAccount);
        List<User> friendsList = testUser.getFriendsList();
        friendsList.add(testFriend);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.findByEmail("testFriend@gmail.com")).thenReturn(
                Optional.ofNullable(testFriend));
        doNothing().when(transactionService).add(new TransactionDTO(10, null,
                TypeOfTransaction.TO_FRIEND, testUser , testFriend));

        userService.send(1, new TransfertDTO(10, null,
                testFriend.getEmail()));
        // Then.
        verify(transactionService, times(1)).add(new TransactionDTO(10, null,
                TypeOfTransaction.TO_FRIEND, testUser, testFriend));
    }

    @Test
    @DisplayName("EnvoyerArgentVersAmi Balance KO test")
    public void testEnvoyerArgentVersAmiBalanceKO() throws Exception {
        // Given.
        testUser.setAccount(account);
        testFriend.setAccount(friendsAccount);
        List<User> friendsList = testUser.getFriendsList();
        friendsList.add(testFriend);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.findByEmail("testFriend@gmail.com")).thenReturn(
                Optional.ofNullable(testFriend));
        // Then.
        assertThrows(Exception.class,
                () ->   userService.send(1, new TransfertDTO(2000000,
                        null,
                        testFriend.getEmail())));
    }
    @Test
    @DisplayName("EnvoyerArgentVersAmi Balance KO test")
    public void testEnvoyerArgentVersAmiUserKO() throws Exception {
        // Given.
        testUser.setAccount(account);
        testFriend.setAccount(friendsAccount);
        List<User> friendsList = testUser.getFriendsList();
        friendsList.add(testFriend);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));

        // Then.
        assertThrows(BadRequestException.class,
                () ->   userService.send(1, new TransfertDTO(2000000,
                        null,
                        "testFriend2@gmail.com")));
    }
    @Test
    @DisplayName("envoyerArgentVersAmi friend list null KO test")
    public void testEnvoyerArgentVersAmiKO() throws Exception {

        // Given.
        testUser.setAccount(account);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.findByEmail("testFriend@gmail.com")).thenReturn(
                Optional.ofNullable(testFriend));
        // Then.
        assertThrows(Exception.class,
                () ->   userService.send(1, new TransfertDTO(10, null,
                        testFriend.getEmail())));
    }

    @Test
    @DisplayName("envoyerArgentVersAmi account KO test")
    public void testEnvoyerArgentVersAmiKOV() throws Exception {

        // Given.
        List<User> friendsList = testUser.getFriendsList();
        friendsList.add(testFriend);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.findByEmail("testFriend@gmail.com")).thenReturn(
                Optional.ofNullable(testFriend));
        // Then.
        assertThrows(Exception.class,
                () ->   userService.send(1, new TransfertDTO(10, null,
                        testFriend.getEmail())));
    }
    @Test
    @DisplayName("envoyerArgentVersAmi friend account KO test")
    public void testEnvoyerArgentVersAmiAccountKO() throws Exception {

        // Given.
        testUser.setAccount(account);
        List<User> friendsList = testUser.getFriendsList();
        friendsList.add(testFriend);
        // When.
        when(userRepository.findById(1)).thenReturn(
                Optional.ofNullable(testUser));
        when(userRepository.findByEmail("testFriend@gmail.com")).thenReturn(
                Optional.ofNullable(testFriend));
        // Then.
        assertThrows(Exception.class,
                () ->   userService.send(1, new TransfertDTO(10, null,
                        testFriend.getEmail())));
    }

    @Test
    @DisplayName("add ok test")
    public void testaddOk() throws Exception {

        // Given.
        userService.add(testUser);
        // Then.
        verify(userRepository, times(1)).save(testUser);

    }
    @Test
    @DisplayName("Get OK test")
    public void testDelete() {

        // Given.

        // When.
        userService.deleteById(1);
        // Then.
        verify(userRepository, times(1)).deleteById(1);
    }
    @Test
    @DisplayName("GetAll OK test")
    public void testGetAll() {

        // Given.
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(testUserDTO);

        // When.
        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toUserDtoList(userList)).thenReturn(userDTOList);

        Iterable<UserDTO> res = userService.getAll();
        // Then.

        int counter = 0;
        for (Object i : res) {
            counter++;
        }
        // Then.
        assertEquals(1, counter);

    }
}
