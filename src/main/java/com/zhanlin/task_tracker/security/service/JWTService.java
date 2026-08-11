package com.zhanlin.task_tracker.security.service;

import com.zhanlin.task_tracker.security.user.CustomUserDetails;

public interface JWTService {

    String generateToken(CustomUserDetails userDetail );

    String extractEmail(String token);

    boolean isTokenValid(String token);
}
