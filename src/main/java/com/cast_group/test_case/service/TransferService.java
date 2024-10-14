package com.cast_group.test_case.service;

import com.cast_group.test_case.comum.AccountUtils;
import com.cast_group.test_case.exception.SaldoInsuficienteException;
import com.cast_group.test_case.exception.TransacaoInvalidaException;
import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private final AccountRepository accountRepository;
    private static final int MAX_RETRIES = 3;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
     public void transferWithRetry(Long fromAccountId, Long toAccountId, double amount) {
        int attempt = 0;
        boolean success = false;
        long backoff = 200;

        while (attempt < MAX_RETRIES && !success) {
            attempt++;
            try {
                executarTransferencia(fromAccountId, toAccountId, amount);
                success = true;
            } catch (ObjectOptimisticLockingFailureException e) {
                logger.warn("Concorrência detectada para a transação entre contas {} e {}. Tentativa falhou.", fromAccountId, toAccountId, e);
                if (attempt == MAX_RETRIES) {
                    throw e;
                }
                try {
                    Thread.sleep(backoff);
                    backoff *= 2;
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrompida durante a retentativa");
                }
            } catch (SaldoInsuficienteException | TransacaoInvalidaException e) {
                throw e;
            }
        }
    }

    @Transactional
    public void executarTransferencia(Long fromAccountId, Long toAccountId, double amount) {
        try {
            Account fromAccount = AccountUtils.getAccount(fromAccountId, accountRepository);
            Account toAccount = AccountUtils.getAccount(toAccountId, accountRepository);

            validarTransferencia(fromAccount, toAccount, amount);

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

        } catch (TransacaoInvalidaException e) {
            System.err.println("Erro na oparação de Credito: " + e.getMessage());
            throw e;
        }
    }

    private void validarTransferencia(Account fromAccount, Account toAccount, double amount) {
        if (amount <= 0) {
            throw new TransacaoInvalidaException("O valor da transferência deve ser positivo.");
        }
        if (fromAccount.equals(toAccount)) {
            throw new TransacaoInvalidaException("A conta de origem e destino devem ser diferentes.");
        }
        if (fromAccount.getBalance() < amount) {
            throw new SaldoInsuficienteException(
                    "A conta " + fromAccount.getId() + " não possui saldo suficiente."
            );
        }
    }
}
