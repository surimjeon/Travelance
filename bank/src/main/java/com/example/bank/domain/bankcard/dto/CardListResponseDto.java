package com.example.bank.domain.bankcard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardListResponseDto {
    private String cardNumber;
    private String cardPassword;
    private String cvc;
    private String cardCoName;
    private Long idx;
    private String cardCoCode;

    public CardListResponseDto (String cardNumber,
                              String cardPassword,
                              String cvc, String cardCoName, Long idx, String cardCoCode){
        this.cardNumber = cardNumber;
        this.cardPassword = cardPassword;
        this.cvc = cvc;
        this.cardCoName = cardCoName;
        this.idx = idx;
        this.cardCoCode = cardCoCode;
    }
}
