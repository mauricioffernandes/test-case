package com.cast_group.test_case.controller;

import com.cast_group.test_case.model.Account;
import com.cast_group.test_case.service.CreditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/credit/{accountId}")
    public ResponseEntity<Account> credit(@PathVariable Long accountId, @RequestParam double amount) {
        Account account = creditService.credit(accountId, amount);
        return ResponseEntity.ok(account);
    }
}
