package com.capstone.banking.service;

import org.springframework.http.ResponseEntity;

import com.capstone.banking.entity.Users;

public interface AuthenticationService 
{

	String registerUser(Users user);

	ResponseEntity<Object> loginUser(Users user);
	
}
