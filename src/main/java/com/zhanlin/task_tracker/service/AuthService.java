package com.zhanlin.task_tracker.service;

import com.zhanlin.task_tracker.dto.AuthAndLoginDTO.AuthResponse;
import com.zhanlin.task_tracker.dto.AuthAndLoginDTO.LoginRequest;
import com.zhanlin.task_tracker.dto.UserDTO.RegisterUserRequest;

public interface AuthService {

    AuthResponse register(RegisterUserRequest request);

    AuthResponse login(LoginRequest request);
}
