package com.example.formscreator.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.formscreator.exception.BadCredentialsException;

@Service
public class JWTService {

    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);

    final Algorithm algorithm = Algorithm.HMAC256("SecretPassword1!");
    final String issuer = "Forms-Creator-Spring-Server";
    final JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
    
    
    public String extractUserEmail(String token){
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            Claim claim = decodedJWT.getClaim("email");
            String userEmail = claim.asString();
            return userEmail;
        } catch (JWTVerificationException e) {
            logger.error("JWT Verification Error");
            throw new BadCredentialsException("");
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userEmail = extractUserEmail(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            Date date = decodedJWT.getExpiresAt();
            return date.before(new Date());
        } catch (JWTVerificationException e) {
            logger.error("JWT Verification Error");
            throw new BadCredentialsException("");
        }
    }

    public String generateToken(UserDetails userDetails){
        return JWT.create()
        .withIssuer(issuer)
        .withSubject(userDetails.getUsername())
        .withClaim("email", userDetails.getUsername())
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .sign(algorithm);
    }
}

