package com.capstone.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.banking.entity.Users;
import com.capstone.banking.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class RegistrationController 
{
	@Autowired
	public UserRepository userRepository;
	
	@Autowired
	public PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public String RegisterUser(@RequestBody Users user)
	{
		if (userRepository.findByUsername(user.getUsername())!=null) {
            return "Username already exists";
        }
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		userRepository.save(user);	
		
		return "User registered successfully";
		
	}
	
	
}
