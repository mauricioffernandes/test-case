package com.cast_group.test_case.controller;

import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.service.DebitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class DebitController {

    private final DebitService debitService;

    public DebitController(DebitService debitService) {
        this.debitService = debitService;
    }

    @PostMapping("/debit/{accountId}")
    public ResponseEntity<Account> debit(@PathVariable Long accountId, @RequestParam double amount) {
        Account account = debitService.debit(accountId, amount);
        return ResponseEntity.ok(account);
    }
}

