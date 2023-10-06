package com.example.bank.domain.bankaccount.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class TransferRequestDto {

    private String depositNumber;
    private Long amount;
    private String memo;
    private LocalDateTime transferAt;
    private String withdrawalNumber;


    public TransferRequestDto(String depositNumber, Long amount, String memo
            , LocalDateTime transferAt, String withdrawalNumber){
        this.depositNumber = depositNumber;
        this.amount = amount;
        this.memo = memo;
        this.transferAt = transferAt;
        this.withdrawalNumber = withdrawalNumber;
    }
}
