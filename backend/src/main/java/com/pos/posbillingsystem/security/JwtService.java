package com.pos.posbillingsystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
@Service
public class JwtService {

    // Secret key (change later and move to application.properties)
	private static final String SECRET =
	        "mysecretkeymysecretkeymysecretkey12345678901234567890";

	private Key getSignKey() {
	    return Keys.hmacShaKeyFor(SECRET.getBytes());
	
    }

    // Generate Token
	public String generateToken(String username) {

	    return Jwts.builder()
	            .setSubject(username)
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
	            .signWith(getSignKey())
	            .compact();
	}

    // Extract Username
	public String extractUsername(String token) {

	    Claims claims = Jwts.parserBuilder()
	            .setSigningKey(getSignKey())
	            .build()
	            .parseClaimsJws(token)
	            .getBody();

	    return claims.getSubject();
	}

    // Validate Token
    public boolean validateToken(String token, String username) {

        String extractedUsername = extractUsername(token);

        return extractedUsername.equals(username);
    }
}