package com.payment.account_service.controller;


import com.payment.account_service.dto.AccountResponse;
import com.payment.account_service.dto.CreateAccountRequest;
import com.payment.account_service.entity.Account;
import com.payment.account_service.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public AccountResponse createAccount(@RequestBody @Valid CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PutMapping("/{id}/deposit")
    public AccountResponse deposit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return accountService.deposit(id, amount);
    }

    @PutMapping("/{id}/withdraw")
    public AccountResponse withdraw(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return accountService.withdraw(id, amount);
    }


}
