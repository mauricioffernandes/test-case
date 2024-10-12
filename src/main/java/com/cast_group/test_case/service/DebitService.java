package com.cast_group.test_case.service;

import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DebitService {

    private final AccountRepository accountRepository;

    public DebitService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account debit(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + accountId));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Saldo insuficiente.");
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }
}

