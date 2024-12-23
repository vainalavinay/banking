package com.capstone.banking.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone.banking.dto.TransactionRequest;
import com.capstone.banking.dto.TransferRequest;
import com.capstone.banking.entity.Account;
import com.capstone.banking.entity.Transaction;
import com.capstone.banking.repository.AccountRepository;
import com.capstone.banking.repository.PersonalDetailsRepository;
import com.capstone.banking.repository.TransactionRepository;
import com.capstone.banking.service.EmailService;
import com.capstone.banking.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService 
{

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	private EmailService emailService;

	
	@Override
	public ResponseEntity<String> deposit(TransactionRequest request) 
	{
		if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) 
		{
            return ResponseEntity.badRequest().body("Amount must be greater than zero");
        }

        Optional<Account> accountOpt = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (accountOpt.isEmpty()) 
        {
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
        emailService.sendNotification(personalDetailsRepository.findByUserUserId(account.getUser().getUserId()).get().getEmail(),
        		"Transaction done",
        		"Rupees "+request.getAmount()+" deposited to your account. "+ "Your current balance is "+account.getBalance()+" rupees.");
        return ResponseEntity.ok("Deposit successful");
	}


	@Override
	public ResponseEntity<String> withdraw(TransactionRequest request) 
	{
		if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) 
		{
            return ResponseEntity.badRequest().body("Amount must be greater than zero");
        }

        Optional<Account> accountOpt = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (accountOpt.isEmpty()) 
        {	
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        Account account = accountOpt.get();
        if (account.getBalance().compareTo(request.getAmount()) < 0) 
        {
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
        emailService.sendNotification(personalDetailsRepository.findByUserUserId(account.getUser().getUserId()).get().getEmail(),
        		"Transaction done",
        		"Rupees "+request.getAmount()+" withdrawn from your account. "+ "Your current balance is "+account.getBalance()+" rupees.");
        return ResponseEntity.ok("Withdrawal successful");
	}


	@Override
	public ResponseEntity<String> transfer(TransferRequest request) 
	{
		if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) 
		{
            return ResponseEntity.badRequest().body("Amount must be greater than zero");
        }

        Optional<Account> fromAccountOpt = accountRepository.findByAccountNumber(request.getFromAccountId());
        Optional<Account> toAccountOpt = accountRepository.findByAccountNumber(request.getToAccountId());

        if (fromAccountOpt.isEmpty() || toAccountOpt.isEmpty()) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("One or both accounts not found");
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) 
        {
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
        
        emailService.sendNotification(personalDetailsRepository.findByUserUserId(toAccount.getUser().getUserId()).get().getEmail(),
        		"Transfer done",
        		"Rupees "+request.getAmount()+" deposited to your account. "+ "Your current balance is "+toAccount.getBalance()+" rupees.");
        
        emailService.sendNotification(personalDetailsRepository.findByUserUserId(fromAccount.getUser().getUserId()).get().getEmail(),
        		"Transaction done",
        		"Rupees "+request.getAmount()+" withdrawn from your account. "+ "Your current balance is "+fromAccount.getBalance()+" rupees.");
        return ResponseEntity.ok("Transfer successful");
        
	}


	@Override
	public ResponseEntity<Object> getTransactionHistory(String accountNumber) 
	{
		Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        List<Transaction> transactions = transactionRepository.findByAccountAccountId(accountOpt.get().getAccountId());
        return ResponseEntity.ok(transactions);
	}

}
