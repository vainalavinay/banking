package com.capstone.banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.banking.entity.PersonalDetails;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> 
{

	Optional<PersonalDetails> findByUserUserId(Long userId);
//	Optional<PersonalDetails> findByUser
	
}
