package com.example.bank.domain.bankaccount.entity;

import com.example.bank.domain.accounter.entity.Accounter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "bankaccount")
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String account;
    private LocalDateTime createdAt;
    private Long balance;
    private String accountPassword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accounter_id")
    private Accounter accounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_name_id")
    private BankName bankName;

    @OneToMany(mappedBy = "bankAccount")
    @Builder.Default
    private List<BankAccountTransfer> bankAccountTransferList = new ArrayList<>();

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", createdAt=" + createdAt +
                ", balance=" + balance +
                ", accountPassword='" + accountPassword + '\'' +
                ", accounter=" + (accounter != null ? accounter.getName() : "null") +
                ", bankName=" + (bankName != null ? bankName.getBankName() : "null") +
                '}';
    }
}
