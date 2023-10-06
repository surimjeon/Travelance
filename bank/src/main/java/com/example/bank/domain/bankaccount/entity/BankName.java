package com.example.bank.domain.bankaccount.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.example.bank.domain.bankaccount.entity.BankAccount;

@Table(name = "bankname")
@Data
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;
    private Long idx;
    private String bankCode;

    @OneToMany(mappedBy = "bankName")
    @Builder.Default
    private List<BankAccount> bankAccountList = new ArrayList<>();
}
