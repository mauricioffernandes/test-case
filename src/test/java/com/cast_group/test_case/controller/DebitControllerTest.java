package com.cast_group.test_case.controller;

import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.service.DebitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DebitControllerTest {

    @InjectMocks
    private DebitController debitController;

    @Mock
    private DebitService debitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDebitSuccess() {
        Account account = new Account(1L, "John Doe", 100.0, 0);
        when(debitService.debit(1L, 50.0)).thenReturn(account);

        ResponseEntity<Account> response = debitController.debit(1L, 50.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testDebitAccountNotFound() {
        when(debitService.debit(1L, 50.0)).thenThrow(new RuntimeException("Conta não encontrada: 1"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            debitController.debit(1L, 50.0);
        });
        assertEquals("Conta não encontrada: 1", exception.getMessage());
    }

    @Test
    void testDebitInsufficientBalance() {
        when(debitService.debit(1L, 50.0)).thenThrow(new RuntimeException("Saldo insuficiente."));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            debitController.debit(1L, 50.0);
        });
        assertEquals("Saldo insuficiente.", exception.getMessage());
    }
}