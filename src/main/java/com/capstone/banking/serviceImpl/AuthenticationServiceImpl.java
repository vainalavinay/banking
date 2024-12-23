package com.capstone.banking.serviceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.banking.dto.AuthResponse;
import com.capstone.banking.entity.Account;
import com.capstone.banking.entity.PersonalDetails;
import com.capstone.banking.entity.Users;
import com.capstone.banking.repository.AccountRepository;
import com.capstone.banking.repository.PersonalDetailsRepository;
import com.capstone.banking.repository.UserRepository;
import com.capstone.banking.security.JwtUtil;
import com.capstone.banking.service.AuthenticationService;
import com.capstone.banking.service.EmailService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService 
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;
	    
	    

    @Override
    public String registerUser(Users user) 
    {
        if (userRepository.findByUsername(user.getUsername()) != null) 
        {
            return "Username already exists";
        }

        if (user.getPassword().length() < 6) 
        {
            return "Password must be at least 6 characters long";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user); 
        generateAccount(user.getUserId());
        return "User registered successfully";
    }

    private void generateAccount(Long userId) 
    {
        Optional<Users> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) 
        {
            Users user = userOptional.get();

            Account defaultAccount = new Account();
            defaultAccount.setAccountNumber(getAccountNumber()); 
            defaultAccount.setBalance(BigDecimal.ZERO);          
            defaultAccount.setIsActive(true);                   
            defaultAccount.setUser(user);                       

            accountRepository.save(defaultAccount);
        } else 
        {
            throw new RuntimeException("User with ID " + userId + " not found");
        }
    }

    private String getAccountNumber() 
    {
        return String.valueOf(System.currentTimeMillis() % 1_000_000_000L);
    }



	@Override
	public ResponseEntity<Object> loginUser(Users user) 
	{
		Users existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not registered");
        }

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(existingUser.getUsername(), existingUser.getRole());
        Long userId = existingUser.getUserId();
        Optional <PersonalDetails> personalDetails = personalDetailsRepository.findByUserUserId(userId);
        if(personalDetails.isPresent())
        {
        	String existingEmail = personalDetails.get().getEmail();
        	if(existingEmail !=null && !existingEmail.isEmpty())
        	{
        		emailService.sendNotification(personalDetails.get().getEmail(), "Login Notification", "You have successfully logged in to your account.");        		
        	}
        }
        return ResponseEntity.ok(new AuthResponse(token));
	}

}
