package com.capstone.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.capstone.banking.entity.Account;
import com.capstone.banking.entity.Transaction;
import com.capstone.banking.repository.AccountRepository;
import com.capstone.banking.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Deposit money into an account
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Amount must be greater than zero");
        }

        Optional<Account> accountOpt = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        Account account = accountOpt.get();
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setType("DEPOSIT");
        transaction.setStatus("SUCCESS");
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        return ResponseEntity.ok("Deposit successful");
    }

    // Withdraw money from an account
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Amount must be greater than zero");
        }

        Optional<Account> accountOpt = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        Account account = accountOpt.get();
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            return ResponseEntity.badRequest().body("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setType("WITHDRAWAL");
        transaction.setStatus("SUCCESS");
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        return ResponseEntity.ok("Withdrawal successful");
    }

    // Transfer money between accounts
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Amount must be greater than zero");
        }

        Optional<Account> fromAccountOpt = accountRepository.findById(request.getFromAccountId());
        Optional<Account> toAccountOpt = accountRepository.findById(request.getToAccountId());

        if (fromAccountOpt.isEmpty() || toAccountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("One or both accounts not found");
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            return ResponseEntity.badRequest().body("Insufficient balance in the source account");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setAccount(fromAccount);
        transaction.setAmount(request.getAmount());
        transaction.setType("TRANSFER");
        transaction.setStatus("SUCCESS");
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        return ResponseEntity.ok("Transfer successful");
    }

    // Get transaction history for an account
//    @GetMapping("/{accountId}")
//    public ResponseEntity<?> getTransactionHistory(@PathVariable Long accountId) {
//        Optional<Account> accountOpt = accountRepository.findById(accountId);
//        if (accountOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
//        }
//
//        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
//        return ResponseEntity.ok(transactions);
//    }
}
