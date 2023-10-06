package com.example.bank.domain.bankcard.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CardPaymentDetailResponseDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime paymentAt;
    private Long paymentAmount;
    private String paymentContent;
    private Long approvalNumber;
    private String storeAddress;
    private String storeSector;

    public CardPaymentDetailResponseDto(LocalDateTime paymentAt,
                                        Long paymentAmount,
                                        String paymentContent,
                                        Long approvalNumber,
                                        String storeAddress,
                                        String storeSector) {
    }

}
