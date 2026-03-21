package com.payment.account_service.service;

import com.payment.account_service.dto.AccountResponse;
import com.payment.account_service.dto.CreateAccountRequest;
import com.payment.account_service.entity.Account;

import java.math.BigDecimal;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest account);
    AccountResponse getAccount(Long id);
    AccountResponse deposit(Long id, BigDecimal amount);
    AccountResponse withdraw(Long id, BigDecimal amount);
    AccountResponse getAccountByAccountNumber(String accountNumber);
}
