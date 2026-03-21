package com.payment.payment_service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private String accountType;
    private String status;
    private LocalDateTime createdAt;
}