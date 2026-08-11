package com.zhanlin.task_tracker.service;

import com.zhanlin.task_tracker.dto.UserDTO.RegisterUserRequest;

public interface AuthService {

    String register(RegisterUserRequest request);
}
