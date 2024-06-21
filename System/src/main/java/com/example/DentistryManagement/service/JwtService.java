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

    public String extractMail(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            logger.error("Error while extracting user mail from token: {}", e.getMessage());
            throw new RuntimeException("Error while extracting user mail from token", e);
        }
    }

    // Claim is a pair of value (a name of value AND the value) inside te Payload of JSON
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            logger.error("Error while extracting claim from token: {}", e.getMessage());
            throw new RuntimeException("Error while extracting claim from token", e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        try {
            return generateToken(new HashMap<>(), userDetails);
        } catch (Exception e) {
            logger.error("Error while generating token: {}", e.getMessage());
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 45))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("Error while generating token: {}", e.getMessage());
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String mail = extractMail(token);
            if (mail.equals(userDetails.getUsername()) && !isTokenExpired(token)) {
                System.out.printf("Validate success");
            } else {
                System.out.printf("Validate fail");
            }
            System.out.println(mail.equals(userDetails.getUsername()) && !isTokenExpired(token));
            return mail.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            logger.error("Error while validating token: {}", e.getMessage());
            throw new RuntimeException("Error while validating token", e);
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    // Parameter with a token and build it with function getSigningKey()
    private Claims extractAllClaims(String token) {
        try {
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
        } catch (Exception e) {
            logger.error("Error while extracting all claims from token: {}", e.getMessage());
            throw new RuntimeException("Error while extracting all claims from token", e);
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        // Creates a new SecretKey instance for use with HMAC-SHA algorithms based
        // on the specified key byte array.
        try {
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.error("Error while getting signing key: {}", e.getMessage());
            throw new RuntimeException("Error while getting signing key", e);
        }
    }

}
