package com.capstone.banking.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.banking.entity.PersonalDetails;
import com.capstone.banking.entity.Users;
import com.capstone.banking.repository.PersonalDetailsRepository;
import com.capstone.banking.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class PersonalDetailsController 
{	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@PostMapping("/personalDetails")
	public ResponseEntity<?> getPersonalDetails(@RequestBody PersonalDetails personalDetails)
	{
		Optional<Users> userOptional = userRepository.findById(personalDetails.getUser().getUserId());		
		if (userOptional.isEmpty()) 
		{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("User with ID " + personalDetails.getUser().getUserId() + " not found");
        }
		
		
		personalDetails.setUser(userOptional.get());
		
		personalDetailsRepository.save(personalDetails);
        return new ResponseEntity<>("Account details added", HttpStatus.OK);
		
 	}
	
	
}
