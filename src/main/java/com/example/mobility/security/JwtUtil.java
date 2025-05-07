/**
 * 
 */
package com.example.mobility.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

	private String SECRET_KEY = "daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb"; // Use a stronger secret key

	// Generate JWT Token
	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	// Extract Username from JWT Token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Extract expiration time from JWT Token
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// Extract any claim using a function
	public <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.resolve(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	// Check if token is expired
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Validate token
	public boolean validateToken(String token, String username) {
		return (username.equals(extractUsername(token)) && !isTokenExpired(token));
	}

	// Functional interface for extracting claims
	@FunctionalInterface
	public interface ClaimsResolver<T> {
		T resolve(Claims claims);
	}
}
