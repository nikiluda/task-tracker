package com.zhanlin.task_tracker.service;


import com.zhanlin.task_tracker.dto.AuthAndLoginDTO.AuthResponse;
import com.zhanlin.task_tracker.dto.AuthAndLoginDTO.LoginRequest;
import com.zhanlin.task_tracker.dto.UserDTO.RegisterUserRequest;
import com.zhanlin.task_tracker.entity.User;
import com.zhanlin.task_tracker.entity.UserRole;
import com.zhanlin.task_tracker.exception.PasswordMismatchException;
import com.zhanlin.task_tracker.exception.UserAlreadyExistException;
import com.zhanlin.task_tracker.mapper.UserMapper;
import com.zhanlin.task_tracker.repository.UserRepository;
import com.zhanlin.task_tracker.security.service.JWTService;
import com.zhanlin.task_tracker.security.user.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jWTService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JWTService jWTService,AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jWTService = jWTService;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public AuthResponse register(RegisterUserRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordMismatchException();
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistException(request.email());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.USER);
        User savedUser = userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);

        String token = jWTService.generateToken(userDetails);
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = jWTService.generateToken(userDetails);

        return new AuthResponse(token);
    }
}
