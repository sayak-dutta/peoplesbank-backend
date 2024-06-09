package com.example.peoplesbank.service;

import com.example.peoplesbank.dto.Response;
import com.example.peoplesbank.model.Account;
import com.example.peoplesbank.model.Beneficiary;
import com.example.peoplesbank.model.Customer;
import com.example.peoplesbank.model.Transaction;
import com.example.peoplesbank.repository.AccountRepository;
import com.example.peoplesbank.repository.BeneficiaryRepository;
import com.example.peoplesbank.repository.CustomerRepository;
import com.example.peoplesbank.repository.TransactionRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;


    @Autowired
    private CustomerRepository customerRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private AccountRepository accountRepository;

    public Response<Transaction> saveTransaction(Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Transaction saved successfully", savedTransaction);
    }

    public Response<List<Transaction>> getTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccount_Id(accountId);
        Collections.reverse(transactions);
        return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Transactions retrieved successfully", transactions);
    }

    public Response<Transaction> getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Transaction found", transaction);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Transaction not found", null);
    }

    public Response<List<Transaction>> getTransactionsByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Customer not found", null);
        }

        List<Transaction> allTransactions = new ArrayList<>();
        for (Account account : customer.getAccounts()) {
            List<Transaction> accountTransactions = transactionRepository.findByAccount_Id(account.getId());
            allTransactions.addAll(accountTransactions);
        }

        Collections.reverse(allTransactions);
        return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Transactions retrieved successfully", allTransactions);
    }

    public Response<Transaction> createTransaction(Long accountId, Transaction transaction) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            if (transaction.getType().equals("DEBIT") && account.getBalance() < transaction.getAmount()) {
                return new Response<>(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Insufficient balance", null);
            }

            transaction.setAccount(account);
            transaction.setDate(new Date());
            if (transaction.getType().equals("DEBIT")) {
                account.setBalance(account.getBalance() - transaction.getAmount());
            } else if (transaction.getType().equals("CREDIT")) {
                account.setBalance(account.getBalance() + transaction.getAmount());
            }
            accountRepository.save(account);
            Transaction savedTransaction = transactionRepository.save(transaction);
            return new Response<>(LocalDateTime.now(), HttpStatus.CREATED.value(), "Transaction created successfully", savedTransaction);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Account not found", null);
    }
    public Response<Transaction> transferMoney(Long accountId, Long beneficiaryId, Double amount, String password) {
        Account senderAccount = accountRepository.findById(accountId).orElse(null);

        if (senderAccount == null) {
            return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Account not found", null);
        }

        Customer customer = senderAccount.getCustomer();
        if (!passwordEncoder.matches(password, customer.getPassword())) {
            return new Response<>(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), "Invalid password", null);
        }

        if (senderAccount.getBalance() < amount) {
            return new Response<>(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Insufficient balance", null);
        }

        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId).orElse(null);
        if (beneficiary == null) {
            return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Beneficiary not found", null);
        }

        // Debit sender's account
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        accountRepository.save(senderAccount);

        // Create DEBIT transaction for sender
        Transaction debitTransaction = new Transaction();
        debitTransaction.setAmount(amount);
        debitTransaction.setType("DEBIT");
        debitTransaction.setAccount(senderAccount);
        debitTransaction.setBeneficiary(beneficiary);
        transactionRepository.save(debitTransaction);

        return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Transfer successful", debitTransaction);
    }
}
