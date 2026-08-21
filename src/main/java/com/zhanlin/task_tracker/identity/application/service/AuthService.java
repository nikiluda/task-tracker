package com.zhanlin.task_tracker.identity.application.service;

import com.zhanlin.task_tracker.identity.api.rest.dto.AuthResponse;
import com.zhanlin.task_tracker.identity.api.rest.dto.LoginRequest;
import com.zhanlin.task_tracker.identity.api.rest.dto.RegisterUserRequest;

public interface AuthService {

    AuthResponse register(RegisterUserRequest request);

    AuthResponse login(LoginRequest request);
}
