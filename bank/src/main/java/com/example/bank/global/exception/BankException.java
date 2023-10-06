package com.example.bank.global.exception;

public class BankException extends RuntimeException {
    private String errorMessage;

    public BankException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
