package com.example.bank.domain.bankcard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentPushResponseDto {
    private String memberPrivateId;
    private String paymentAt;
    private Long paymentAmount;
    private String paymentContent;
    private String storeSector;
    private String storeAddress;
}