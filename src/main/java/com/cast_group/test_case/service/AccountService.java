package com.cast_group.test_case.service;

import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}

