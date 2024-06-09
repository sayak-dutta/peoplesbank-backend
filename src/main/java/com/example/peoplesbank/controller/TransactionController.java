package com.example.peoplesbank.controller;

import com.example.peoplesbank.dto.Response;
import com.example.peoplesbank.dto.TransferRequest;
import com.example.peoplesbank.model.Account;
import com.example.peoplesbank.model.Beneficiary;
import com.example.peoplesbank.model.Transaction;
import com.example.peoplesbank.service.AccountService;
import com.example.peoplesbank.service.BeneficiaryService;
import com.example.peoplesbank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @PostMapping("/{accountId}")
    public ResponseEntity<Response<Transaction>> createTransaction(@PathVariable Long accountId, @RequestBody Transaction transaction) {
        Response<Transaction> response = transactionService.createTransaction(accountId, transaction);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Transaction>> getTransactionById(@PathVariable Long id) {
        Response<Transaction> response = transactionService.getTransactionById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Response<List<Transaction>>> getTransactionsByAccountId(@PathVariable Long accountId) {
        Response<List<Transaction>> response = transactionService.getTransactionsByAccountId(accountId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Response<List<Transaction>>> getTransactionsByCustomerId(@PathVariable Long customerId) {
        Response<List<Transaction>> response = transactionService.getTransactionsByCustomerId(customerId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
@PostMapping("/transfer")
public ResponseEntity<Response<Transaction>> transferMoney(@RequestBody TransferRequest transferRequest) {
    Response<Transaction> response = transactionService.transferMoney(
            transferRequest.getAccountId(),
            transferRequest.getBeneficiaryId(),
            transferRequest.getAmount(),
            transferRequest.getPassword()
    );
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
}
}
