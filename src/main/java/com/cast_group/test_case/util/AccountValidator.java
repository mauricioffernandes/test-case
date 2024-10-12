package com.cast_group.test_case.util;

import com.cast_group.test_case.exception.SaldoInsuficienteException;
import com.cast_group.test_case.exception.TransacaoInvalidaException;
import com.cast_group.test_case.model.Account;

public class AccountValidator {

    public static void validarTrasacao(Account account, double amount) {
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
