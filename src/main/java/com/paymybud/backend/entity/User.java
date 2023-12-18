package com.paymybud.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String email;
    private String password;
    private String token;

    @OneToOne(fetch=FetchType.EAGER , cascade=CascadeType.ALL , orphanRemoval=true)
    @JoinColumn(name = "accountid")
    private Account account;

    @ManyToMany(cascade =  CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinTable(name="friends",
            joinColumns={@JoinColumn(name="user_id",referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="friend_id")}
    )
    public List<User> friendsList = new ArrayList<>();



}
