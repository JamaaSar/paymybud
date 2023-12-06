package com.paymybud.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "amount")
    private Integer amount;
    @Column(name = "message")
    private String message;
    @Enumerated(EnumType.STRING)
    private TypeOfTransaction typeOfTransaction;

    @ManyToOne
    @JoinColumn(name = "senderid")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiverid")
    private User receiver;


}
