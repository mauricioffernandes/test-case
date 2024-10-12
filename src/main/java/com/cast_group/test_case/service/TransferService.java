package com.cast_group.test_case.service;

import com.cast_group.test_case.exception.SaldoInsuficienteException;
import com.cast_group.test_case.exception.TransacaoInvalidaException;
import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, double amount) {
        try {
            Account fromAccount = obterConta(fromAccountId, "Conta de origem não encontrada: " + fromAccountId);
            Account toAccount = obterConta(toAccountId, "Conta de destino não encontrada: " + toAccountId);

            validarTransferencia(fromAccount, toAccount, amount);

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

        } catch (SaldoInsuficienteException | TransacaoInvalidaException e) {
            System.err.println("Erro na transferência: " + e.getMessage());
            throw e;  // Propaga para rollback automático
        } catch (Exception e) {
            System.err.println("Erro inesperado durante a transferência: " + e.getMessage());
            throw new RuntimeException("Erro interno na transferência");
        }
    }

    private Account obterConta(Long accountId, String mensagemErro) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new TransacaoInvalidaException(mensagemErro));
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