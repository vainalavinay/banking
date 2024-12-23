package com.capstone.banking.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone.banking.entity.Account;
import com.capstone.banking.repository.AccountRepository;
import com.capstone.banking.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService 
{
	
	@Autowired
    private AccountRepository accountRepository;


	@Override
	public ResponseEntity<Account> getaccountDetails(String accountNumber) 
	{
		Optional<Account> userAccount = accountRepository.findByAccountNumber(accountNumber.toString());
		System.out.println("Account from Repository: " + userAccount + accountNumber);

		if (userAccount.isEmpty()) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
		
		return ResponseEntity.ok(userAccount.get());
	}
		
		
		
		

//	@Autowired
//	private UserRepository userRepository;
		
//	@Override
//	public ResponseEntity<String> accountDetails(Account account) 
//	{
//        Optional<Users> userOptional = userRepository.findById(account.getUser().getUserId());
//        
//        if (userOptional.isEmpty()) 
//        {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                                 .body("User with ID " + account.getUser().getUserId() + " not found");
//        }
//        
//        account.setUser(userOptional.get());
//        accountRepository.save(account);
//        return new ResponseEntity<>("Account details added", HttpStatus.OK);
//
//	}
}
