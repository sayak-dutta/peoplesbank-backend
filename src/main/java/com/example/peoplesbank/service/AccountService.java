package com.example.peoplesbank.service;

import com.example.peoplesbank.model.Account;
import com.example.peoplesbank.model.Customer;
import com.example.peoplesbank.repository.AccountRepository;
import com.example.peoplesbank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Account createAccount(Long customerId, Account account) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            account.setCustomer(customer);
            return accountRepository.save(account);
        }
        return null;
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Account deposit(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            return accountRepository.save(account);
        }
        return null;
    }
}






