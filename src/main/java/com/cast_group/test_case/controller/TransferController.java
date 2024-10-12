package com.cast_group.test_case.controller;

import com.cast_group.test_case.service.AccountService;
import com.cast_group.test_case.service.TransferService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    // Transferir valor entre contas
    @PostMapping
    public ResponseEntity<String> transfer(@RequestParam Long fromAccountId,
                                           @RequestParam Long toAccountId,
                                           @RequestParam double amount) {
        try {
            transferService.transfer(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok("Transferência realizada com sucesso.");
        } catch (ConstraintViolationException e) {
            throw new RuntimeException("Database constraint violated: " + e.getMessage(), e);
        }
    }
}
