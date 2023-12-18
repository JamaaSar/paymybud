package com.paymybud.backend.dto;

import com.paymybud.backend.entity.Account;
import com.paymybud.backend.entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Generated
public class UserDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String token;
    private Account account;
    public List<FriendDTO> friendsList = new ArrayList<>();


}
