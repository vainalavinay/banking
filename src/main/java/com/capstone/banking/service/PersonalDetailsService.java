package com.capstone.banking.service;

import org.springframework.http.ResponseEntity;

import com.capstone.banking.entity.PersonalDetails;

public interface PersonalDetailsService 
{

	ResponseEntity<String> getPersonalDetails(PersonalDetails personalDetails);

	ResponseEntity<String> updatePersonalDetails(PersonalDetails personalDetails);

}
