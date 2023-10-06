package com.example.bank.domain.bankaccount.dto.response;

import lombok.Data;

@Data
public class WonResponseDto {
    private String privateId;

    public WonResponseDto(String privateId) {
        this.privateId = privateId;
    }

}
