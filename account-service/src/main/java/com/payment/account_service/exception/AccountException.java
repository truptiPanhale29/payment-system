package com.payment.account_service.exception;

public class AccountException extends RuntimeException{

    private final int statusCode;

    public AccountException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
