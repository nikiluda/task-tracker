package com.zhanlin.task_tracker.identity.application.service;

import com.zhanlin.task_tracker.identity.api.rest.dto.RegisterUserRequest;
import com.zhanlin.task_tracker.dto.UserDTO.UserResponse;
import com.zhanlin.task_tracker.identity.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterUserRequest request) {
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(request.password());

        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail());
    }



}
