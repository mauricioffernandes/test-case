package com.cast_group.test_case.controller;

import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.service.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreditControllerTest {

    @InjectMocks
    private CreditController creditController;

    @Mock
    private CreditService creditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreditSuccess() {
        // Dado
        Account account = new Account(1L, "John Doe", 100.0, 0);
        when(creditService.credit(1L, 50.0)).thenReturn(account);

        ResponseEntity<Account> response = creditController.credit(1L, 50.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testCreditAccountNotFound() {
        when(creditService.credit(1L, 50.0)).thenThrow(new RuntimeException("Conta não encontrada: 1"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            creditController.credit(1L, 50.0);
        });
        assertEquals("Conta não encontrada: 1", exception.getMessage());
    }
}