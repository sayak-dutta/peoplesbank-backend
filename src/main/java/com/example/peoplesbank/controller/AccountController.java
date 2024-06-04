package com.example.peoplesbank.controller;

import com.example.peoplesbank.model.Account;
import com.example.peoplesbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/{customerId}")
    public ResponseEntity<Account> createAccount(@PathVariable Long customerId, @RequestBody Account account) {
        Account newAccount = accountService.createAccount(customerId, account);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<Account> deposit(@PathVariable Long accountId, @RequestParam Double amount) {
        Account updatedAccount = accountService.deposit(accountId, amount);
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}
