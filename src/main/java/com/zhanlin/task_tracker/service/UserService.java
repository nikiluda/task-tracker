package com.zhanlin.task_tracker.service;

import com.zhanlin.task_tracker.dto.UserDTO.RegisterUserRequest;
import com.zhanlin.task_tracker.dto.UserDTO.UserResponse;
import com.zhanlin.task_tracker.entity.User;
import com.zhanlin.task_tracker.mapper.UserMapper;
import com.zhanlin.task_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public UserResponse createUser(RegisterUserRequest request) {

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);

    }
}
