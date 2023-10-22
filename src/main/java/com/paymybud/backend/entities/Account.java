package com.paymybud.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "accounts")
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "balance")
    private Integer balance;

    @Column(name = "iban", unique = true)
    private String iban;
    @OneToMany(mappedBy = "sender")
    private List<Payment> sendedPayments = new ArrayList<>();
    @OneToMany(mappedBy = "reciever")
    private List<Payment> recievedPayments = new ArrayList<>();


}
