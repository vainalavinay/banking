package com.capstone.banking.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.banking.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> 
{

	Optional<Account> findByAccountNumber(String accountNumber);
//	Optional<Account> findByUsers_Id(Long usersId);
}
