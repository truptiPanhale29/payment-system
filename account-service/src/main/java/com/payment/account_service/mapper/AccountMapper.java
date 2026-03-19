package com.payment.account_service.mapper;

import com.payment.account_service.dto.AccountResponse;
import com.payment.account_service.dto.CreateAccountRequest;
import com.payment.account_service.entity.Account;

public class AccountMapper {

    public static Account toEntity(CreateAccountRequest request) {
        return Account.builder()
                .ownerName(request.getOwnerName())
                .balance(request.getBalance())
                .accountType(request.getAccountType())
                .build();
    }

    public static AccountResponse toResponse(Account account){
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .ownerName(account.getOwnerName())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
