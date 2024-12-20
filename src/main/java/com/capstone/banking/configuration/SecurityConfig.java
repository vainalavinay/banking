package com.capstone.banking.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.capstone.banking.service.CustomDetailsService;


@Configuration
public class SecurityConfig {
	
	private final CustomDetailsService userDetailsService;

    public SecurityConfig(CustomDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

	
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

	   
	   
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) // Disables CSRF for simplicity (consider enabling it in production)
        		
        		.headers(headers -> headers
        		.frameOptions(frameOptions -> frameOptions
        		.sameOrigin())) // or .deny() or .allowFrom("https://example.com")
        		.authorizeHttpRequests(authorize -> authorize
        				
                .requestMatchers("/auth/register").permitAll() // Open registration endpoint
                .requestMatchers("/admin").hasRole("ADMIN") // Only ADMIN can access /admin
//                .requestMatchers("/h2-console/**").hasRole("ADMIN")
//                .requestMatchers("/user").hasAnyRole("USER", "ADMIN") // Both USER and ADMIN can access /user
//                .requestMatchers("/").permitAll() // The home page is open to all users
                .anyRequest().authenticated() // Any other request requires authentication
            
        	)
            .formLogin(Customizer.withDefaults()) // Enables default login page
            .httpBasic(Customizer.withDefaults()) // Enables HTTP Basic authentication

        .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoder for encoding passwords
    }

}


//	@Bean
//	UserDetailsService userDetailsService()
//	{
//		UserDetails admin = User.builder()
//				.username("vinay")
//				.password(passwordEncoder().encode("vinay123"))
//				.roles("ADMIN")
//				.build();
//		
//		UserDetails user = User.builder()
//				.username("swathi")
//				.password(passwordEncoder().encode("swathi123"))
//				.roles("USER")
//				.build();
//		
//		return new InMemoryUserDetailsManager(admin, user);
//	}