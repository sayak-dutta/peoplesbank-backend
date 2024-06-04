package com.example.peoplesbank.controller;

import com.example.peoplesbank.model.Account;
import com.example.peoplesbank.model.Beneficiary;
import com.example.peoplesbank.model.Transaction;
import com.example.peoplesbank.service.AccountService;
import com.example.peoplesbank.service.BeneficiaryService;
import com.example.peoplesbank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Transaction> createTransaction(@PathVariable Long accountId, @RequestBody Transaction transaction) {
        Transaction newTransaction = transactionService.createTransaction(accountId, transaction);
        return ResponseEntity.ok(newTransaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferMoney(@RequestParam Long accountId,
                                                     @RequestParam Long beneficiaryId,
                                                     @RequestParam Double amount) {
        Account senderAccount = accountService.getAccountById(accountId);
        Beneficiary beneficiary = beneficiaryService.getBeneficiaryById(beneficiaryId);

        // Ensure sender has enough balance
        if (senderAccount.getBalance() < amount) {
            return ResponseEntity.badRequest().body(null);
        }

        // Debit sender's account
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        accountService.saveAccount(senderAccount);

        // Create DEBIT transaction for sender
        Transaction debitTransaction = new Transaction();
        debitTransaction.setAmount(amount);
        debitTransaction.setType("DEBIT");
        debitTransaction.setAccount(senderAccount);
        transactionService.saveTransaction(debitTransaction);

        // Credit beneficiary's account
        Account beneficiaryAccount = accountService.getAccountByAccountNumber(beneficiary.getAccountNumber());
        beneficiaryAccount.setBalance(beneficiaryAccount.getBalance() + amount);
        accountService.saveAccount(beneficiaryAccount);

        // Create CREDIT transaction for beneficiary
        Transaction creditTransaction = new Transaction();
        creditTransaction.setAmount(amount);
        creditTransaction.setType("CREDIT");
        creditTransaction.setAccount(beneficiaryAccount);
        transactionService.saveTransaction(creditTransaction);

        return ResponseEntity.ok(debitTransaction);
    }
}

