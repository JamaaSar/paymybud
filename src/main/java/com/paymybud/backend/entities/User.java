package com.paymybud.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountid")
    private Account account;

    @ManyToMany
    @JoinTable(name="friends",
            joinColumns={@JoinColumn(name="userid")},
            inverseJoinColumns={@JoinColumn(name="friendid")}
    )
    public List<User> friends = new ArrayList<>();



}
