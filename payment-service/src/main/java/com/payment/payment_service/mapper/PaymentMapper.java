package com.payment.payment_service.mapper;

import com.payment.payment_service.dto.PaymentRequest;
import com.payment.payment_service.dto.PaymentResponse;
import com.payment.payment_service.entity.Payment;

public class PaymentMapper {

    public static Payment toEntity (PaymentRequest request) {
        return Payment.builder()
                .fromAccount(request.getFromAccount())
                .toAccount(request.getToAccount())
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();
    }

    public static PaymentResponse toResponse (Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .description(payment.getDescription())
                .createdAt(payment.getCreatedAt())
                .toAccount(payment.getToAccount())
                .fromAccount(payment.getFromAccount())
                .build();
    }
}
