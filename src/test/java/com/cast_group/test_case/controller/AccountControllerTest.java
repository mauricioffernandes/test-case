package com.cast_group.test_case.controller;

import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        Account account = new Account(1L, "John Doe", 100.0, 0);
        when(accountService.createAccount(account)).thenReturn(account);

        ResponseEntity<Account> response = accountController.createAccount(account);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testGetAccountByIdFound() {
        Account account = new Account(1L, "John Doe", 100.0, 0);
        when(accountService.getAccountById(1L)).thenReturn(account);

        ResponseEntity<Account> response = accountController.getAccountById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testGetAccountByIdNotFound() {
        when(accountService.getAccountById(1L)).thenReturn(null);

        ResponseEntity<Account> response = accountController.getAccountById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllAccounts() {
        List<Account> accounts = Arrays.asList(
                new Account(1L, "John Doe", 100.0, 0),
                new Account(2L, "Jane Doe", 200.0, 0)
        );
        when(accountService.getAllAccounts()).thenReturn(accounts);

        ResponseEntity<List<Account>> response = accountController.getAllAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
    }
}