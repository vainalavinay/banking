package com.capstone.banking.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.banking.dto.AccountRequest;
import com.capstone.banking.entity.Account;
import com.capstone.banking.service.AccountService;

@RestController
//@RequestMapping("/user")
public class AccountController 
{
	
    @Autowired
    private AccountService accountService;

	@GetMapping("/user/account")
    public ResponseEntity<Account> getaccountDetails(@RequestBody AccountRequest accountRequest) 
    {
        return accountService.getaccountDetails(accountRequest.getAccountNumber());
    }
    
    
    
//	
//    @PreAuthorize("hasRole('ADMIN')")
//	@PostMapping("/admin/account")
//    public ResponseEntity<String> accountDetails(@RequestBody Account account) 
//	{
//		return accountService.accountDetails(account);	
//    }


}
