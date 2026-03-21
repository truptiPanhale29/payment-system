package com.payment.payment_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotBlank(message = "from account is required")
    private String fromAccount;

    @NotBlank(message = "to account is required")
    private String toAccount;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0",inclusive = false,message = "Amount must be greater than zero")
    private BigDecimal amount;

    private String description;

}
