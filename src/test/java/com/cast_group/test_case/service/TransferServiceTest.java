package com.cast_group.test_case.service;

import com.cast_group.test_case.exception.SaldoInsuficienteException;
import com.cast_group.test_case.exception.TransferenciaInvalidaException;
import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveTransferirComSucesso() {
        Account fromAccount = new Account(1L, "Jurema",1000.0, 0);
        Account toAccount = new Account(2L, "Francisco Raimundo", 500.0, 0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        transferService.transfer(1L, 2L, 200.0);

        assertEquals(800.0, fromAccount.getBalance());
        assertEquals(700.0, toAccount.getBalance());
        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    void deveLancarSaldoInsuficienteException() {
        Account fromAccount = new Account(1L, "Paula Sofia", 100.0, 0);
        Account toAccount = new Account(2L, "Malaquias Sofrencia", 500.0, 0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        SaldoInsuficienteException exception = assertThrows(
                SaldoInsuficienteException.class,
                () -> transferService.transfer(1L, 2L, 200.0)
        );
        assertEquals("A conta 1 não possui saldo suficiente.", exception.getMessage());
    }

    @Test
    void deveLancarRuntimeExceptionParaContaNaoEncontrada() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> transferService.transfer(1L, 2L, 100.0)
        );
        assertEquals("Conta de origem não encontrada: 1", exception.getMessage());
    }

    @Test
    void deveLancarTransferenciaInvalidaExceptionParaMesmaConta() {
        Account account = new Account(1L, "Nonato Raimundo", 1000.0, 0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        TransferenciaInvalidaException exception = assertThrows(
                TransferenciaInvalidaException.class,
                () -> transferService.transfer(1L, 1L, 100.0)
        );
        assertEquals("A conta de origem e destino devem ser diferentes.", exception.getMessage());
    }
}
