package com.cast_group.test_case.service;

import com.cast_group.test_case.comum.AccountUtils;
import com.cast_group.test_case.exception.SaldoInsuficienteException;
import com.cast_group.test_case.exception.TransacaoInvalidaException;
import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DebitService {

    private final AccountRepository accountRepository;

    public DebitService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account debit(Long accountId, double amount) {
        try {
            Account account = AccountUtils.getAccount(accountId, accountRepository);

            validaDebit(account, amount);

            account.setBalance(account.getBalance() - amount);
            return accountRepository.save(account);

        } catch (SaldoInsuficienteException | TransacaoInvalidaException e) {
            System.err.println("Erro na oparação de Credito: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro interno na trasação");
        }
    }

    private void validaDebit(Account account, double amount) {
        if (amount <= 0) {
            throw new TransacaoInvalidaException("O valor deve ser positivo.");
        }
        if (account.getBalance() < amount) {
            throw new SaldoInsuficienteException(
                    "A conta " + account.getId() + " não possui saldo suficiente."
            );
        }
    }
}

