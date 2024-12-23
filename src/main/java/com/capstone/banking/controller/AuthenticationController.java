package com.capstone.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.banking.entity.Users;
import com.capstone.banking.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController 
{
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @PostMapping("/register")
    public String registerUser(@RequestBody Users user) 
    {
        return authenticationService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody Users user) 
    {
        return authenticationService.loginUser(user);
    }
    
    

}
