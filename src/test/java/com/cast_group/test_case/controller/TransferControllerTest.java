package com.cast_group.test_case.controller;

import com.cast_group.test_case.service.TransferService;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class TransferControllerTest {

    @InjectMocks
    private TransferController transferController;

    @Mock
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransferSuccess() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        double amount = 100.0;

        ResponseEntity<String> response = transferController.transfer(fromAccountId, toAccountId, amount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transferência realizada com sucesso.", response.getBody());
        verify(transferService, times(1)).transferWithRetry(fromAccountId, toAccountId, amount);
    }

    @Test
    void testTransferConstraintViolationException() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        double amount = 100.0;

        doThrow(new ConstraintViolationException("Saldo insuficiente", null, "saldo"))
                .when(transferService).transferWithRetry(fromAccountId, toAccountId, amount);

        RuntimeException exception =
                org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                        () -> transferController.transfer(fromAccountId, toAccountId, amount));

        assertEquals("Database constraint violated: Saldo insuficiente", exception.getMessage());
        verify(transferService, times(1)).transferWithRetry(fromAccountId, toAccountId, amount);
    }
}