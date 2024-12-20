package com.capstone.banking.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.capstone.banking.entity.Users;
import com.capstone.banking.repository.UserRepository;
import org.springframework.security.core.userdetails.User;

@Service
public class CustomDetailsService implements UserDetailsService 
{

	private final UserRepository userRepository;
	public CustomDetailsService(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if(user == null)
        {
        	throw new UsernameNotFoundException("User not found");
        }
        
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", "")) // Remove "ROLE_" prefix for Spring Security
                .build();
    }

}
