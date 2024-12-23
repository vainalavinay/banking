package com.capstone.banking.service;

import org.springframework.http.ResponseEntity;

import com.capstone.banking.dto.TransactionRequest;
import com.capstone.banking.dto.TransferRequest;

public interface TransactionService 
{

	ResponseEntity<String> deposit(TransactionRequest request);

	ResponseEntity<String> withdraw(TransactionRequest request);

	ResponseEntity<String> transfer(TransferRequest request);

	ResponseEntity<Object> getTransactionHistory(String accountNumber);
	
}
