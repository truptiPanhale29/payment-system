package com.payment.payment_service.exception;

public class PaymentException extends RuntimeException{

    private final int statusCode;
    public PaymentException(int statusCode, String message){
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
