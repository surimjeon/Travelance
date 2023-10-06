package com.example.bank.domain.bankaccount.dto.response;

import com.example.bank.domain.accounter.entity.Accounter;
import com.example.bank.domain.bankaccount.entity.BankAccount;
import com.example.bank.domain.bankaccount.entity.BankName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BankAccountResponseDto {
    private String account;
    private LocalDateTime createdAt;
    private Long balance;
    private String accounterPrivateId;
    private String bankName;
    private Long idx;

    public BankAccountResponseDto(String account, LocalDateTime createdAt, Long balance, String accounterPrivateId, String bankName, Long idx) {
        this.account = account;
        this.createdAt = createdAt;
        this.balance = balance;
        this.accounterPrivateId = accounterPrivateId;
        this.bankName = bankName;
        this.idx = idx;
    }
}