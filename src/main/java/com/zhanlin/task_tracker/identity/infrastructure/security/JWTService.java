package com.zhanlin.task_tracker.identity.infrastructure.security;

public interface JWTService {

    String generateToken(CustomUserDetails userDetail );

    String extractEmail(String token);

    boolean isTokenValid(String token);
}
