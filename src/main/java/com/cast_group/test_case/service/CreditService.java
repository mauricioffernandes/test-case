package com.cast_group.test_case.service;

import com.cast_group.test_case.exception.SaldoInsuficienteException;
import com.cast_group.test_case.exception.TransacaoInvalidaException;
import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import com.cast_group.test_case.util.AccountValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

    private final AccountRepository accountRepository;

    public CreditService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account credit(Long accountId, double amount) {
        try {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + accountId));

            AccountValidator.validarTrasacao(account, amount);

            account.setBalance(account.getBalance() + amount);
            return accountRepository.save(account);

        } catch (TransacaoInvalidaException e) {
            System.err.println("Erro na oparação de Credito: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado durante a trasação: " + e.getMessage());
            throw new RuntimeException("Erro interno na trasação");
        }
    }
}

