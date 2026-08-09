package com.zhanlin.task_tracker.mapper;

import com.zhanlin.task_tracker.dto.UserDTO.RegisterUserRequest;
import com.zhanlin.task_tracker.dto.UserDTO.UserResponse;
import com.zhanlin.task_tracker.entity.User;
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
