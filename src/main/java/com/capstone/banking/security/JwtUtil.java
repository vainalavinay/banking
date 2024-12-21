package com.capstone.banking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private SecretKey secretKey;

    @Value("${jwt.secret}")
    private String secretKeyProperty;

    // Initialize the secret key after the bean is created using @PostConstruct
    @PostConstruct
    public void init() {
        // Check if the secret key property is provided and not empty
        if (secretKeyProperty != null && !secretKeyProperty.isEmpty()) {
            // Check the length of the secret key (for example, using 256-bit key)
            if (secretKeyProperty.length() < 32) {
                throw new IllegalArgumentException("JWT secret key must be at least 32 characters long!");
            }
            // Generate the SecretKey using the provided secret string
            this.secretKey = Keys.hmacShaKeyFor(secretKeyProperty.getBytes());
        } else {
            throw new IllegalArgumentException("JWT secret key not provided in application.properties!");
        }
    }

    // Updated generateToken method with additional claims and signature algorithm
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        long expirationTime = 1000 * 60 * 60; // 1 hour
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        // Using signWith() with SecretKey and SignatureAlgorithm
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Extract the username (subject) from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract claims from the JWT token using a custom claims resolver
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract roles/authorities from the JWT token
    public List<GrantedAuthority> extractAuthorities(String token) {
        final Claims claims = extractAllClaims(token);
        String role = claims.get("role", String.class);
        return List.of(new SimpleGrantedAuthority(role));  // Wrap the role as SimpleGrantedAuthority
    }

    // Check if the token is expired
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Validate the token based on username and expiration
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    // Refresh the token with updated expiration
    public String refreshToken(String token) {
        String username = extractUsername(token);
        Map<String, Object> claims = extractAllClaims(token);
        claims.put("role", claims.get("role"));  // Optionally modify claims if needed
        
        long expirationTime = 1000 * 3 * 60; // 1 hour
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
