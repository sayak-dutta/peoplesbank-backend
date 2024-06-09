package com.example.peoplesbank.service;

import com.example.peoplesbank.dto.Response;
import com.example.peoplesbank.model.Account;
import com.example.peoplesbank.model.Customer;
import com.example.peoplesbank.model.Transaction;
import com.example.peoplesbank.repository.AccountRepository;
import com.example.peoplesbank.repository.CustomerRepository;
import com.example.peoplesbank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Response<Account> createAccount(Long customerId, Account account) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            account.setCustomer(customer);
            Account savedAccount = accountRepository.save(account);
            return new Response<>(LocalDateTime.now(), HttpStatus.CREATED.value(), "Account created successfully", savedAccount);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Customer not found", null);
    }

    public Response<Account> getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Account found", account);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Account not found", null);
    }

    public Response<List<Account>> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Accounts retrieved successfully", accounts);
    }

    public Response<List<Account>> getAllAccountsByCustomerId(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        if (accounts != null && !accounts.isEmpty()) {
            return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Accounts retrieved successfully", accounts);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "No accounts found for the customer", null);
    }

    public Response<Account> saveAccount(Account account) {
        Account savedAccount = accountRepository.save(account);
        return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Account saved successfully", savedAccount);
    }

    public Response<Account> getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Account found", account);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Account not found", null);
    }

    public Response<Account> deposit(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            Account updatedAccount = accountRepository.save(account);
            Transaction creditTransaction = new Transaction();
            creditTransaction.setAmount(amount);
            creditTransaction.setType("CREDIT");
            creditTransaction.setDate(new Date());
            creditTransaction.setAccount(updatedAccount);
            transactionRepository.save(creditTransaction);
            return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Deposit successful", updatedAccount);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Account not found", null);
    }
}
