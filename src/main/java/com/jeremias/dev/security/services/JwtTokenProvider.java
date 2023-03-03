package com.jeremias.dev.security.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Service
public class JwtTokenProvider {
	
	
	@Value("${app.jwt-secret}")
	private String jwtSecret;
	//
	@Value("${app.jwt-experation-milliseconds}")
	private int jwtExperationMs;
	
	public int getJwtExperationMs() {
		return jwtExperationMs;
	}
	//we 
	public String extractUsername(String token) {
		//Obtemos el username //recordar FUNCTION<T>
	    return extractClaim(token, Claims::getSubject);
	  }
	 public String generateToken(
			 UserDetails userDetails) {
		    return generateToken(new HashMap<>(), userDetails);
	 }
	

 
 public String generateToken(Map<String, Object> claims,
		 UserDetails userDetails) {
	 Date currentDate = new Date(); //instance of date equal to today date
		Date expireDate = new Date( currentDate.getTime()+jwtExperationMs);
	 return Jwts
			 .builder()
			 .setClaims(claims)
			 .setSubject(userDetails.getUsername())
				.setIssuedAt(currentDate)
				.setExpiration(expireDate)
			.signWith(getSignInKey(),SignatureAlgorithm.HS256)
			.compact();
 }
 
	//claim are head data from jwt
 //Here we create a function which contains a token and a function which are from claims, that way we can get 
 //we need like 
 //	fUNCTION <OUPUT,INPUT>
 ///JSON web tokens (JWTs) claims are pieces of information asserted about a subject.
	 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		 //oBTEMOS EL CLAIMS DEL TOKEN  
		 final Claims claims = extractAllClaims(token);
		    return claimsResolver.apply(claims);
		  }

	 
	public Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
				
	}
	public Key getSignInKey() {
		System.out.println("asd"+ jwtSecret);
		byte[] keyBites = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(keyBites);
	}
	 public boolean isTokenValid(String token, UserDetails userDetails) {
		 //el usuaio que obtuvimos del token
		    final String username = extractUsername(token);
		    //Se compara con el obtenido de la bd y se validad que nos este expirado
		    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
		  }
	 
	  private boolean isTokenExpired(String token) {
		    return extractExpiration(token).before(new Date());
		  }
	  
	  private Date extractExpiration(String token) {
		    return extractClaim(token, Claims::getExpiration);
		  }
	
}
