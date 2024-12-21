package com.capstone.banking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    // Hardcoded secret key (consider storing it securely in a configuration file or environment variable)
    private static final String SECRET = "secretsecretsecretsecretsecretsecret";

    public JwtUtil() {
        this.secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
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
        
        long expirationTime = 1000 * 60 * 60; // 1 hour
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
