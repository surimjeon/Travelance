package com.example.bank.domain.bankaccount.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DetailResponseDto {
    private String depositNumber;
    private Long amount;
    private String memo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime transferAt;
    private String withdrawalNumber;
    private String classification;


    public DetailResponseDto(String depositNumber, Long amount, String memo
    , LocalDateTime transferAt, String withdrawalNumber, String classification){
        this.depositNumber = depositNumber;
        this.amount = amount;
        this.memo = memo;
        this.transferAt = transferAt;
        this.withdrawalNumber = withdrawalNumber;
        this.classification = classification;
    }
}
