package com.example.bank.domain.bankaccount.dto.request;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OneRequestDto {
    private String name;
    private String bankName;
    private String account;
}
