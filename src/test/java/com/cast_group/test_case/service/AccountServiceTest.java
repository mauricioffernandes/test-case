package com.cast_group.test_case.service;

import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        Account account = new Account(1L, "John Doe", 100.0, 0);
        when(accountRepository.save(account)).thenReturn(account);

        Account createdAccount = accountService.createAccount(account);

        assertEquals(account, createdAccount);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testGetAccountByIdFound() {
        Account account = new Account(1L, "John Doe", 100.0, 0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account foundAccount = accountService.getAccountById(1L);

        assertNotNull(foundAccount);
        assertEquals(account, foundAccount);
    }

    @Test
    void testGetAccountByIdNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Account account = accountService.getAccountById(1L);

        assertNull(account);
    }

    @Test
    void testGetAllAccounts() {
        List<Account> accounts = Arrays.asList(
                new Account(1L, "John Doe", 100.0, 0),
                new Account(2L, "Jane Doe", 200.0, 0)
        );
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();

        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findAll();
    }
}