package com.example.bank.domain.bankaccount.dto.response;

import lombok.Data;


@Data
public class OneResponseDto {
    private String verifyCode;


    public OneResponseDto(String verifyCode) {

        this.verifyCode = verifyCode;
    }


}
