package com.example.bank.domain.bankaccount.dto.response;

import lombok.Data;

@Data
public class BankCodeResponseDto {

    private String cardCoName;
    private Long idx;
    private String cardCoCode;

    public BankCodeResponseDto (String cardCoName, Long idx, String cardCoCode) {
        this.cardCoName = cardCoName;
        this.idx= idx;
        this.cardCoCode= cardCoCode;
    }
}
