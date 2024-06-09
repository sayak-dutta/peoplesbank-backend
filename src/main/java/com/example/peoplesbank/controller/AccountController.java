package com.example.peoplesbank.controller;

import com.example.peoplesbank.dto.Response;
import com.example.peoplesbank.model.Account;
import com.example.peoplesbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/{customerId}")
    public ResponseEntity<Response<Account>> createAccount(@PathVariable Long customerId, @RequestBody Account account) {
        Response<Account> response = accountService.createAccount(customerId, account);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<Response<Account>> deposit(@PathVariable Long accountId, @RequestParam Double amount) {
        Response<Account> response = accountService.deposit(accountId, amount);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Response<List<Account>>> getAllAccountsByCustomerId(@PathVariable Long customerId) {
        Response<List<Account>> response = accountService.getAllAccountsByCustomerId(customerId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Account>> getAccountById(@PathVariable Long id) {
        Response<Account> response = accountService.getAccountById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping
    public ResponseEntity<Response<List<Account>>> getAllAccounts() {
        Response<List<Account>> response = accountService.getAllAccounts();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
