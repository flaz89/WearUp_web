package com.wearup.wearup.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wearup.wearup.brand.Brand;
import com.wearup.wearup.exception.UnauthorizedException;
import com.wearup.wearup.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JWTTools {

	@Value("${spring.jwt.secret}")
	private String secret;
	
	// ---------------------------------------- CREAZIONE TOKEN PER UTENTI E BRAND

//	public String createUserToken(User u) {
//		String token = Jwts
//				.builder()
//				.setSubject(u.getId().toString()) 
//				.claim("entityType", "User")
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
//				.signWith(Keys.hmacShaKeyFor(secret.getBytes())) 
//				.compact();
//		return token;
//	}
	
	public String createUserToken(User u) {
	    String entityType = "User"; // Default value
	    if ("ADMIN".equals(u.getRole().toString())) { // Supponendo che getRole() restituisca il ruolo dell'utente
	        entityType = "Admin";
	    }
	    
	    String token = Jwts
	            .builder()
	            .setSubject(u.getId().toString())
	            .claim("entityType", entityType) // Imposta entityType in base al ruolo
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
	            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
	            .compact();
	    return token;
	}

	
	public String createBrandToken(Brand b) {
		String token = Jwts
				.builder()
				.setSubject(String.valueOf(b.getId()))
				.claim("entityType", "Brand")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
				.signWith(Keys.hmacShaKeyFor(secret.getBytes())) 
				.compact();
		return token;
	}
	
	public void verifyToken(String token) {
		try {
			Jwts
			.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
			.build()
			.parse(token);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new UnauthorizedException("Token is invalid! try to log in again.");
		}
	}

	public String extractSubject(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public String extractEntityType(String token) {
		
	    Claims claims = Jwts
	    		.parserBuilder()
	            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
	            .build()
	            .parseClaimsJws(token)
	            .getBody();

	    return claims.get("entityType", String.class);
	}
	
	
}
