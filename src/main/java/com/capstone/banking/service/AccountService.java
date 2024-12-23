package com.capstone.banking.service;

import org.springframework.http.ResponseEntity;

import com.capstone.banking.entity.Account;

public interface AccountService 
{

//	ResponseEntity<String> accountDetails(Account account);

	ResponseEntity<Account> getaccountDetails(String accountNumber);

}
