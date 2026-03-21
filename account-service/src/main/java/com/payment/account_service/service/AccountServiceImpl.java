package com.payment.account_service.service;

import com.payment.account_service.dto.AccountResponse;
import com.payment.account_service.dto.CreateAccountRequest;
import com.payment.account_service.entity.Account;
import com.payment.account_service.entity.AccountStatus;
import com.payment.account_service.exception.AccountException;
import com.payment.account_service.mapper.AccountMapper;
import com.payment.account_service.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = AccountMapper.toEntity(request);
        account.setAccountNumber("ACC"+System.currentTimeMillis());
        account.setStatus(AccountStatus.ACTIVE);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.toResponse(savedAccount);
    }

    @Override
    public AccountResponse getAccount(Long id) {
        Account account = accountRepository.findById(id).
                orElseThrow(()-> new AccountException(404, "Account not found with id: " + id));
        return AccountMapper.toResponse(account);
    }

    @Override
    public AccountResponse deposit(Long id, BigDecimal amount){
        Account account = accountRepository.findById(id)
                 .orElseThrow(() -> new AccountException(404, "Account not found with id: " + id));
        account.setBalance(account.getBalance().add(amount));
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.toResponse(savedAccount);
    }

    @Override
    public AccountResponse withdraw(Long id, BigDecimal amount){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException(404, "Account not found with id: " + id));
        if(account.getBalance().compareTo(amount)<0){
            throw new AccountException(400, "Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(amount));
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.toResponse(savedAccount);
    }

    @Override
    public AccountResponse getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(404, "Account not found with account number: " + accountNumber));
        return AccountMapper.toResponse(account);
    }
}


