package com.paymybud.backend.dto;

import com.paymybud.backend.entities.Account;
import com.paymybud.backend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


public record AccountDto ( Integer balance, String iban){ }
