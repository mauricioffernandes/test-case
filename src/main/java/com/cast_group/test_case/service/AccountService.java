package com.cast_group.test_case.service;

import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Transactional
    public Account creditAccount(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + accountId));
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    @Transactional
    public Account debitAccount(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + accountId));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Saldo insuficiente.");
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

//    @Transactional
//    public void transfer(Long fromAccountId, Long toAccountId, double amount) {
//        Account fromAccount = accountRepository.findById(fromAccountId)
//                .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + fromAccountId));
//        Account toAccount = accountRepository.findById(toAccountId)
//                .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + toAccountId));
//
//        if (fromAccount.getBalance() < amount) {
//            throw new RuntimeException("Saldo insuficiente.");
//        }
//
//        // Deduz valor da conta de origem
//        fromAccount.setBalance(fromAccount.getBalance() - amount);
//        // Credita valor na conta de destino
//        toAccount.setBalance(toAccount.getBalance() + amount);
//
//        // Salvar as alterações
//        accountRepository.save(fromAccount);
//        accountRepository.save(toAccount);
//    }

    // Listar uma conta por ID
    @Transactional
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    // Listar todas as contas
    @Transactional
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}

