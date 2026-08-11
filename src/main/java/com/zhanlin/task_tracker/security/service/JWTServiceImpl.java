package com.zhanlin.task_tracker.security.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.zhanlin.task_tracker.security.user.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;

public class JWTServiceImpl implements JWTService {

    private final Algorithm algorithm;
    private final Long expiration;

    public JWTServiceImpl(
            @Value("${jwt.secret") String secret,
            @Value("${jwt.expiration}") Long expiration) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.expiration = expiration;
    }

    @Override
    public String generateToken(CustomUserDetails userDetail) {
        return "";
    }

    @Override
    public String extractEmail(String token) {
        return "";
    }

    @Override
    public boolean isTokenValid(String token) {
        return false;
    }
}
