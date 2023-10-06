package com.example.bank.domain.bankaccount.dto.request;

import lombok.Data;

@Data
public class BalanceRequestDto {
    String privateId;
    String account;

    public BalanceRequestDto(String privateId, String account) {
        this.privateId = privateId;
        this.account = account;
    }
}
