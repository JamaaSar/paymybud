package com.paymybud.backend.dto;

import com.paymybud.backend.entities.Account;
import com.paymybud.backend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private Account account;
    public List<User> friends = new ArrayList<>();


}
