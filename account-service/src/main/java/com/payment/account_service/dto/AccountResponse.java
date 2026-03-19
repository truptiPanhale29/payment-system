package com.payment.account_service.dto;


import com.payment.account_service.entity.AccountStatus;
import com.payment.account_service.entity.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private AccountType accountType;
    private AccountStatus status;

}
