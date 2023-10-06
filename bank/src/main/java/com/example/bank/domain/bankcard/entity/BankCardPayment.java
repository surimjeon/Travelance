package com.example.bank.domain.bankcard.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "bankcardpayment")
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCardPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime paymentAt;
    private Long paymentAmount;
    private String paymentContent;
    private Long approvalNumber;
    private String storeAddress;
    private String storeSector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_card_id")
    private BankCard bankCard;

    @Override
    public String toString() {
        return "BankCardPayment{paymentAt=" + paymentAt + ", paymentAmount=" + paymentAmount + ", paymentContent='" + paymentContent + "', storeAddress='" + storeAddress + "', storeSector='" + storeSector + "'}";
    }
}
