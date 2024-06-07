package com.example.DentistryManagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Encryption key HS256
    @Value("${secret_key.hs256}")
    private String SECRET_KEY;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    public String extractPlayerName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Claim is a pair of value (a name of value AND the value) inside te Payload of JSON
    public <T> T extractClaim (String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractPlayerName(token);
        return (userName.equals(userDetails.getUsername())) && ! isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    // Parameter with a token and build it with function getSigningKey()
    private Claims extractAllClaims(String token) {
        // Use the JWT parser builder to create a parser
        return Jwts
                .parserBuilder()
                // Set the signing key used to verify the token
                .setSigningKey(getSigningKey())
                // Build the parser
                .build()
                // Parse the token and retrieve the body, which contains the claims
                .parseClaimsJws(token)
                // Get the body of the parsed token, which contains the claims
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        // Creates a new SecretKey instance for use with HMAC-SHA algorithms based
        // on the specified key byte array.
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
