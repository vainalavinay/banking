package com.capstone.banking.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.banking.entity.PersonalDetails;
import com.capstone.banking.service.PersonalDetailsService;

@RestController
@RequestMapping("/user")
public class PersonalDetailsController 
{	
	
	@Autowired
	private PersonalDetailsService personalDetailsService;
	
	@PostMapping("/personalDetails")
	public ResponseEntity<String> getPersonalDetails(@RequestBody PersonalDetails personalDetails)
	{
		return personalDetailsService.getPersonalDetails(personalDetails);
		
 	}
	
	@PutMapping("/updateDetails")
	public ResponseEntity<?> updatePersonalDetails(@RequestBody PersonalDetails personalDetails) 
	{	
		return personalDetailsService.updatePersonalDetails(personalDetails);	
	}

	
	
	
	
}
