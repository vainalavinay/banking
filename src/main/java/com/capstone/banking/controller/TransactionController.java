package com.capstone.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.banking.dto.TransactionRequest;
import com.capstone.banking.dto.TransferRequest;
import com.capstone.banking.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/transactions")
public class TransactionController 
{

	@Autowired
    private TransactionService transactionService;

	
    // Deposit money into an account
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest request) 
    {
        return transactionService.deposit(request);
    }

    // Withdraw money from an account
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionRequest request) 
    {
        return transactionService.withdraw(request);
    }

    // Transfer money between accounts
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) 
    {
        return transactionService.transfer(request);
    }

    //Get transaction history for an account
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<Object> getTransactionHistory(@PathVariable String accountNumber) 
    {
        return transactionService.getTransactionHistory(accountNumber);
    }
    
    
    
    
    
    
    
    
    
    
    
//    @PostMapping("/scheduleTransfer")
//	public ResponseEntity<?> scheduleTransfer(@RequestBody ScheduleTransfer scheduleTransfer)
//	{
//		return 
//	}
    
    
    
    
    
    
    
}
