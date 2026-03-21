package com.payment.payment_service.dto;

import com.payment.payment_service.entity.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private Long id;
    private String paymentId;
    private PaymentStatus status;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;


}
