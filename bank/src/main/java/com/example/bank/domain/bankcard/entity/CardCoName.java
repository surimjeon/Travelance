package com.example.bank.domain.bankcard.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "cardconame")
@Data
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardCoName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardCoName;
    private Long idx;
    private String cardCoCode;

    @OneToMany(mappedBy = "cardCoName")
    @Builder.Default
    private List<BankCard> bankCardList = new ArrayList<>();
}
