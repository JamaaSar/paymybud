package com.paymybud.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybud.backend.dto.CredentialsDTO;
import com.paymybud.backend.dto.SignUpDTO;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Transactional
@SpringBootTest
public class AuthControllerIT {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Registration OK test")
    public void registrationTest() throws Exception {

        final var firstname = "Test";
        final var lastName = "Test";
        final var email = "test@gmail.com";
        final var password = "token";

        mockMvc.perform(post("/api/v1/signup")
               .with(csrf())
               .content(asJsonString(new SignUpDTO(firstname, lastName, email,
                       password)))
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(status().isCreated());
   }
    @Test
    @Sql(scripts = "classpath:/user.sql")
    @DisplayName("Login KO test")
    public void loginTest() throws Exception {



        final var email = "user@gmail.com";
        final var password = "test1234";

        mockMvc.perform(post("/api/v1/login")
                        .with(csrf())
                        .content(asJsonString(new CredentialsDTO(email, password)))
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


