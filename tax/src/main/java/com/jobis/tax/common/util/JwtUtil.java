package com.jobis.tax.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyBase64;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyBase64);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1시간 유효

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
//    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    private final long expirationTime = 1000 * 60 * 60 * 10; // 10 시간
//
//
//    // 토큰에서 만료 시간 추출
//    public Date extractExpiration(String token) {
//        Claims claims = extractAllClaims(token);
//        return claims.getExpiration();
//    }
//
//    // 모든 클레임 추출
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//
//    // 토큰 생성
//    public String generateToken(String userId) {
//        return Jwts.builder()
//                .setSubject(userId)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(secretKey)
//                .compact();
//    }
//
//    // 토큰 유효성 확인
//    public boolean validateToken(String token, String userId) {
//        String extractedUserId = extractUserId(token);
//        return (extractedUserId.equals(userId) && !isTokenExpired(token));
//    }
//    // 토큰에서 사용자명 추출
//    public String extractUserId(String token) {
//        Claims claims = extractAllClaims(token);
//        return claims.getSubject();
//    }
//    // 토큰 만료 여부 확인
//    private boolean isTokenExpired(String token) {
//        Date expiration = extractExpiration(token);
//        return expiration.before(new Date());
//    }


}