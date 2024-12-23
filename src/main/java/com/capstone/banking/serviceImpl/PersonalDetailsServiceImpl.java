package com.capstone.banking.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone.banking.entity.PersonalDetails;
import com.capstone.banking.entity.Users;
import com.capstone.banking.repository.PersonalDetailsRepository;
import com.capstone.banking.repository.UserRepository;
import com.capstone.banking.service.EmailService;
import com.capstone.banking.service.PersonalDetailsService;

@Service
public class PersonalDetailsServiceImpl implements PersonalDetailsService 
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	private EmailService emailService;

	
	@Override
	public ResponseEntity<String> getPersonalDetails(PersonalDetails personalDetails) 
	{
		Optional<Users> userOptional = userRepository.findById(personalDetails.getUser().getUserId());		
		if (userOptional.isEmpty()) 
		{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("User with ID " + personalDetails.getUser().getUserId() + " not found");
        }
		
		personalDetails.setUser(userOptional.get());
		
		personalDetailsRepository.save(personalDetails);
        return new ResponseEntity<>("Personal details added", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> updatePersonalDetails(PersonalDetails personalDetails) 
	{
		Optional<PersonalDetails> existingDetailsOptional = personalDetailsRepository.findByUserUserId(personalDetails.getUser().getUserId());

	    if (existingDetailsOptional.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body("No personal details found for User with ID " + personalDetails.getUser().getUserId());
	    }

	    PersonalDetails existingDetails = existingDetailsOptional.get();

	    existingDetails.setAddress(personalDetails.getAddress());
	    existingDetails.setPhoneNumber(personalDetails.getPhoneNumber());
	    existingDetails.setEmail(personalDetails.getEmail());
	    existingDetails.setName(personalDetails.getName());

	    personalDetailsRepository.save(existingDetails);
	    emailService.sendNotification(existingDetails.getEmail(), "Profile Update Notification", "Profile Updated Successfully");
	    return new ResponseEntity<>("Personal details updated successfully", HttpStatus.OK);
	}

}
