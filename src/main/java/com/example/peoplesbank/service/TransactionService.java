package com.example.peoplesbank.service;

import com.example.peoplesbank.model.Account;
import com.example.peoplesbank.model.Transaction;
import com.example.peoplesbank.repository.AccountRepository;
import com.example.peoplesbank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccount_Id(accountId);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }
    public Transaction createTransaction(Long accountId, Transaction transaction) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            transaction.setAccount(account);
            transaction.setDate(new Date());
            if (transaction.getType().equals("DEBIT")) {
                account.setBalance(account.getBalance() - transaction.getAmount());
            } else if (transaction.getType().equals("CREDIT")) {
                account.setBalance(account.getBalance() + transaction.getAmount());
            }
            accountRepository.save(account);
            return transactionRepository.save(transaction);
        }
        return null;
    }



}

