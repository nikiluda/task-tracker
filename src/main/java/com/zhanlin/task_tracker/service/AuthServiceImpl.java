package com.zhanlin.task_tracker.service;


import com.zhanlin.task_tracker.dto.UserDTO.RegisterUserRequest;
import com.zhanlin.task_tracker.exception.PasswordMismatchException;
import com.zhanlin.task_tracker.exception.UserAlreadyExistException;
import com.zhanlin.task_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{


    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(RegisterUserRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordMismatchException();
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistException(request.email());
        }

        return null;
    }
}
