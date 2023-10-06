package com.example.bank.domain.bankaccount.entity.type;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Column(name = "account")
    private String value;

    private Account(final String value) {
        this.value = value;
    }

    public static Account from(String account) {
        return new Account(account);
    }
}
