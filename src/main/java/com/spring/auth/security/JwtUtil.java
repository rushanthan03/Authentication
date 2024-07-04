package com.spring.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.auth.entity.AuthUserDetail;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${auth.web.jwtSecret}")
    private String jwtSecret;
    @Autowired
    private ObjectMapper objectMapper;

    public JwtUtil() {
    }

    public String extractPhoneNumber(String token) {
        return (String) this.extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return (Date) this.extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return (Claims) Jwts.parserBuilder().setSigningKey(this.getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.getSignKey()).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException var3) {
            log.error("Invalid JWT token: {}", var3.getMessage());
        } catch (ExpiredJwtException var4) {
            log.error("JWT token is expired: {}", var4.getMessage());
        } catch (UnsupportedJwtException var5) {
            log.error("JWT token is unsupported: {}", var5.getMessage());
        } catch (IllegalArgumentException var6) {
            log.error("JWT claims string is empty: {}", var6.getMessage());
        }

        return false;
    }

    public String GenerateToken(AuthUserDetail userDetails) {
        Map<String, Object> claims = new HashMap();
        claims.put("id", userDetails.getId());
        claims.put("roles", userDetails.getAuthorities());
        return this.createToken(claims, userDetails.getPhoneNo());
    }

    private String createToken(Map<String, Object> claims, String phoneNumber) {
        return Jwts.builder().setIssuer("auth").setIssuedAt(new Date()).setSubject(phoneNumber).addClaims(claims).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 172800000L)).signWith(this.getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = (byte[]) Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
