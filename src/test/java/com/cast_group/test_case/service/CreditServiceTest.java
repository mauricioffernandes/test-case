package com.cast_group.test_case.service;

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

class CreditServiceTest {

    @InjectMocks
    private CreditService creditService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreditSuccess() {
        Account account = new Account(1L, "John Doe", 100.0, 0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account creditedAccount = creditService.credit(1L, 50.0);

        assertEquals(150.0, creditedAccount.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testCreditAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            creditService.credit(1L, 50.0);
        });
        assertEquals("Erro interno na trasação", exception.getMessage());
    }
}