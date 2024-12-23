package com.capstone.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.banking.service.TransactionService;

@RestController
@RequestMapping("/admin/transactions")
public class AdminController 
{
	
	@Autowired
	private TransactionService transactionService;
	
	
    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/history/{accountNumber}")
    public ResponseEntity<Object> getTransactionHistory(@PathVariable String accountNumber) 
    {
        return transactionService.getTransactionHistory(accountNumber);
    }
}
