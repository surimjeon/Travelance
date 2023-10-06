package com.example.bank.domain.bankcard.entity;

import com.example.bank.domain.accounter.entity.Accounter;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "bankcard")
@Data
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true)
    private String cardNumber;
    private String cardPassword;
    @Column (unique = true)
    private String cvc;

    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accounter_id")
    private Accounter accounter;

    @OneToMany(mappedBy = "bankCard")
    @Builder.Default
    private List<BankCardPayment> bankCardPaymentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_co_name_id")
    private CardCoName cardCoName;



    @Override
    public String toString() {
        return "BankCard{id=" + id + ", cardNumber='" + cardNumber + "', cvc='" + cvc + "'}";
    }
}
