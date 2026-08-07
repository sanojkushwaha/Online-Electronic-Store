package com.webapp.onlineelectronicstore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//This is used to perform jwt operation
//jwt generate :- user name retrive karne ke liye
@Component
public class JwtHelper {

    //requirements :-> 1.> validity (in milies second)
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    //secret key
    @Value("${jwt.secret}")
    private String secret;

    //retrive  username from token
    public String extractUsername(String token) {
        return extractAllClaim(token, Claims::getSubject);
    }

    private <T> T extractAllClaim(String token,
                               Function<Claims, T> claimResolver) {

        Claims claims = extractClaim(token);
        return claimResolver.apply(claims);
    }


    //for retrieveing any information from token we will need the secret key
    private Claims extractClaim(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //check if the token has expired
    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration((token));
        return expiration.before(new Date());
    }

    //retrive expiration date from jwt token
    public Date extractExpiration(String token) {
        return extractAllClaim(token, Claims::getExpiration);
    }

    //generate token from user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    //validation
    public boolean validateToken(String token, UserDetails userDetails) {

        try {
            String username = extractUsername(token);

            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);

        } catch (Exception e) {
            return false;
        }
    }
}
