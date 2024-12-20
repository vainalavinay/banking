package com.capstone.banking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController 
{
	 	@GetMapping("/")
	    public String home() {
	        return "Welcome to the public page!";
	    }

	    @GetMapping("/user")
	    public String user() {
	        return "Welcome User!";
	    }

	    @GetMapping("/admin")
	    public String admin() {
	        return "Welcome Admin!";
	    }
}
