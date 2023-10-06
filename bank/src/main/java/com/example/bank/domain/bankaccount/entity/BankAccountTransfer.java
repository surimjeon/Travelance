package com.example.bank.domain.bankaccount.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "bankaccounttransver")
@Data
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime transferAt;
    private String depositNumber;
    private String withdrawalNumber;
    private String memo;
    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
}
