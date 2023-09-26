package com.paymybud.backend.entities;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "amount")
    private Long amount;
    @Enumerated(EnumType.STRING)
    private TypeOfPayment typeOfPayment;

    @ManyToOne
    @JoinColumn(name = "senderid")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "recieverid")
    private Account reciever;

}
