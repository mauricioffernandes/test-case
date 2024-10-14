package com.cast_group.test_case.service;

import com.cast_group.test_case.comum.AccountUtils;
import com.cast_group.test_case.exception.TransacaoInvalidaException;
import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

    private final AccountRepository accountRepository;

    public CreditService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public Account credit(Long accountId, double amount) {
        try {
            Account account = AccountUtils.getAccount(accountId, accountRepository);

            validaCredit(amount);

            account.setBalance(account.getBalance() + amount);
            return accountRepository.save(account);

        } catch (TransacaoInvalidaException e) {
            System.err.println("Erro na oparação de Credito: " + e.getMessage());
            throw e;
        } catch (Exception e) {
                   throw new RuntimeException("Erro interno na trasação");
        }
    }

    private void validaCredit(double amount) {
        if (amount <= 0) {
            throw new TransacaoInvalidaException("O valor deve ser positivo.");
        }
    }
}

