package com.example.universitycrud.config;

import com.example.universitycrud.sevice.impl.MyUserDetailsImpl;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtCore {
    @Value("${universitycrud.app.secret}")
    private String secret_code;

    @Value("${universitycrud.app.lifetime}")
    private int lifetime;

    private final RedisTemplate<String, String> redisTemplate;

    public JwtCore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateToken(Authentication authentication) {
        MyUserDetailsImpl myUserDetails;
        myUserDetails = (MyUserDetailsImpl) authentication.getPrincipal();
        String username = myUserDetails.getUsername();
        String token = Jwts.builder()
                .setSubject((username))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + lifetime))
                .signWith(Keys.hmacShaKeyFor(secret_code.getBytes(StandardCharsets.UTF_8)))
                .compact();

        redisTemplate.opsForValue().set(
                username,
                token,
                lifetime,
                TimeUnit.MILLISECONDS
        );

        return token;

    }
    public String getNameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret_code.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret_code.getBytes()))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            //TODO
        }
        return false;
    }

    public void invalidateToken(String token) {
        try {
            String username = getNameFromJwt(token);
            redisTemplate.delete(getRedisKey(username));
        } catch (Exception e) {
            //TODO
        }
    }

    public String getRedisKey(String username) {
        return "jwt: " + username;
    }
}
