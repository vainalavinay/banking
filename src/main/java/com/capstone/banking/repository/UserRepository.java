package com.capstone.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.banking.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> 
{
	<Optional>Users findByUsername(String username);
}
