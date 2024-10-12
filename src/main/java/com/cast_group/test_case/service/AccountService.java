package com.cast_group.test_case.service;

import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Transactional
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}

