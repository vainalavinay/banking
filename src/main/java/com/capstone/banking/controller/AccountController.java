package com.capstone.banking.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.banking.entity.Account;
import com.capstone.banking.entity.Users;
import com.capstone.banking.repository.AccountRepository;
import com.capstone.banking.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AccountController 
{
	@Autowired
    private AccountRepository accountRepository;
	
    @Autowired
    private UserRepository userRepository;
	@PostMapping("/account")
    public ResponseEntity<?> accountDetails(@RequestBody Account account) {
        // Fetch the Users entity using the provided user ID
        Optional<Users> userOptional = userRepository.findById(account.getUser().getUserId());
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("User with ID " + account.getUser().getUserId() + " not found");
        }
        
        // Set the Users entity in the Account
        account.setUser(userOptional.get());
        
        // Save the Account entity
        accountRepository.save(account);
        
        return new ResponseEntity<>("Account details added", HttpStatus.OK);
    }

}
