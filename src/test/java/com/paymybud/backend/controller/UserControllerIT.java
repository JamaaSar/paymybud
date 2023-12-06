package com.paymybud.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybud.backend.dto.AccountDTO;
import com.paymybud.backend.dto.TransfertDTO;
import com.paymybud.backend.entity.Account;
import com.paymybud.backend.entity.User;
import com.paymybud.backend.exceptions.BadRequestException;
import com.paymybud.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@Transactional
@SpringBootTest
public class UserControllerIT {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @Test
    @Sql(scripts = "classpath:/user.sql")
    @DisplayName("get test")
    public void getTest() throws Exception {

        mockMvc.perform(get("/api/v1/user/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
    @Test
    @Sql(scripts = "classpath:/user.sql")
    @DisplayName("get all test")
    public void getAllTest() throws Exception {

        mockMvc.perform(get("/api/v1/user/allUser/1")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "classpath:/user.sql")
    @DisplayName("delete test")
    public void deleteTest() throws Exception {

        mockMvc.perform(delete("/api/v1/user/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
    @Test
    @Sql(scripts = "classpath:/user.sql")
    @DisplayName("Registration  test")
    public void addFriendTest() throws Exception {

        mockMvc.perform(post("/api/v1/user/1/addFriend/friend@gmail.com")
               .with(csrf())
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(status().isOk());
   }
    @Test
    @Sql(scripts = "classpath:/user.sql")
    @DisplayName("Login KO test")
    public void addAccountTest() throws Exception {

        Integer balance = 100;
        String iban = "iban1";

        mockMvc.perform(post("/api/v1/user/2/createAccount")
                        .with(csrf())
                        .content(asJsonString(new AccountDTO(balance, iban)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "classpath:/user.sql")
    @DisplayName("Login KO test")
    public void transfererArgentTest() throws Exception {

        Account account = new Account();
        account.setBalance(1000);
        account.setIban("iban");
        User user = userRepository.findById(1)
                .orElseThrow(() -> new BadRequestException("Utilisateur inconnu"));
        user.setAccount(account);


        mockMvc.perform(post("/api/v1/user/1/transfert/100")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "classpath:/user.sql")
    @DisplayName("Login KO test")
    public void envoyerArgentTest() throws Exception {

        Account account = new Account();
        account.setBalance(1000);
        account.setIban("iban");
        User user = userRepository.findById(1)
                .orElseThrow(() -> new BadRequestException("Utilisateur inconnu"));
        user.setAccount(account);


        mockMvc.perform(post("/api/v1/user/1/charger/100")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
    @Test
    @Sql(scripts = "classpath:/user.sql")
    @DisplayName("Login KO test")
    public void sendTest() throws Exception {

        Integer amount = 100;
        String description = "hi";
        String recieverEmail = "friend@gmail.com";
        Account account = new Account();
        account.setBalance(1000);
        account.setIban("iban");
        Account accountFr = new Account();
        accountFr.setBalance(1000);
        accountFr.setIban("ibanFr");
        User user = userRepository.findById(1)
                .orElseThrow(() -> new BadRequestException("Utilisateur inconnu"));
        user.setAccount(account);

        User userFr = userRepository.findById(2)
                .orElseThrow(() -> new BadRequestException("Utilisateur inconnu"));
        userFr.setAccount(accountFr);
        List<User> friendList = user.getFriendsList();
        friendList.add(userFr);


        mockMvc.perform(post("/api/v1/user/1/send")
                        .with(csrf())
                        .content(asJsonString(new TransfertDTO(amount, description,
                                recieverEmail)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }


