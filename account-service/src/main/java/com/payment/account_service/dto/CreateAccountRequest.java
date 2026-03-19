package com.payment.account_service.dto;
import com.payment.account_service.entity.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {

    @NotBlank(message = "Owner name is required")
    private String ownerName;

   @NotNull(message = "Initial balance is required")
   @DecimalMin(value = "0.0",inclusive = false,message = "Balance must be greater than zero")
   private BigDecimal balance;

    @NotNull(message = "Account type is required")
    private AccountType accountType;


}