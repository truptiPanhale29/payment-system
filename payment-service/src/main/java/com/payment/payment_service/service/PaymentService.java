package com.payment.payment_service.service;


import com.payment.payment_service.dto.PaymentRequest;
import com.payment.payment_service.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse initiatePayment(PaymentRequest request);
    PaymentResponse getPayment(String paymentId);
}
