package com.example.bank.domain.bankcard.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CardPaymentDetailRequestDto {
    private String privateId;
    private LocalDate startDate;
    private LocalDate endDate;

}
