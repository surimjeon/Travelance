package com.example.bank.domain.accounter.entity;

import com.example.bank.domain.bankaccount.entity.BankAccount;
import com.example.bank.domain.bankcard.entity.BankCard;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "accounter")
@Data
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Accounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column (unique = true)
    private String privateId;

    private String fcmToken;

    @OneToMany(mappedBy = "accounter")
    @Builder.Default
    private List<BankAccount> bankAccountList = new ArrayList<>();

    @OneToMany(mappedBy = "accounter")
    @Builder.Default
    private List<BankCard> bankCardList = new ArrayList<>();

    @Override
    public String toString() {
        return "Accounter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", privateId='" + privateId + '\'' +
                '}';
    }
}
