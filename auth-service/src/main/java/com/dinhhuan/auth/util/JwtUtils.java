package com.dinhhuan.auth.util;


import com.dinhhuan.auth.model.UserIdDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);
    private static final String CLAIM_KEY_USER_ID = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    private String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                .subject(claims.get(CLAIM_KEY_USER_ID).toString())
                .claim(CLAIM_KEY_CREATED, claims.get(CLAIM_KEY_CREATED).toString())
                .expiration(generateExpirationDate())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
    private Date generateExpirationDate(){
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
    public Claims getClaimsFromToken(String token){
        Claims claims = null;
        try{
            claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch(Exception e){
            LOGGER.error("Failed to get claims", e);
        }
        return claims;
    }
    public Long getUserIdFromToken(String token){
        Long id;
        try{
            Claims claims = getClaimsFromToken(token);
            id = Long.parseLong(claims.getSubject());
        } catch(Exception e){
            id = null;
        }
        return id;
    }
    public boolean validateToken(String token, UserDetails userDetails){
        Long id = getUserIdFromToken(token);
        return ((UserIdDetails) userDetails).getUserId() == id && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token){
        Date expiredDate = getExpirationDateFromToken(token);
        return expiredDate.before(new Date());
    }
    private Date getExpirationDateFromToken(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ID, ((UserIdDetails) userDetails).getUserId());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}
