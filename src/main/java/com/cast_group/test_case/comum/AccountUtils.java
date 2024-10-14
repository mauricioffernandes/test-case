package com.cast_group.test_case.comum;

import com.cast_group.test_case.exception.TransacaoInvalidaException;
import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;

public class AccountUtils {
    public static Account getAccount(Long accountId, AccountRepository accountRepository) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new TransacaoInvalidaException("Conta não encontrada: " + accountId));
    }
}
