package com.example.bank.domain.bankaccount.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BalanceResponseDto {
    String account;
    Long balance;



    public BalanceResponseDto(String account, Long balance) {
        this.account = account;
        this.balance = balance;
    }
}
