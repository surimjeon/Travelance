package com.example.bank.domain.bankcard.dto;

import lombok.Data;

@Data
public class ResultDto {
    String result;

    public ResultDto(String result){
        this.result = result;
    }
}
