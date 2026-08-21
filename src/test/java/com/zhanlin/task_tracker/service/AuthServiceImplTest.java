package com.zhanlin.task_tracker.service;

import com.zhanlin.task_tracker.identity.api.rest.dto.AuthResponse;
import com.zhanlin.task_tracker.identity.api.rest.dto.LoginRequest;
import com.zhanlin.task_tracker.identity.api.rest.dto.RegisterUserRequest;
import com.zhanlin.task_tracker.identity.application.service.AuthServiceImpl;
import com.zhanlin.task_tracker.identity.domain.User;
import com.zhanlin.task_tracker.exception.PasswordMismatchException;
import com.zhanlin.task_tracker.exception.UserAlreadyExistException;
import com.zhanlin.task_tracker.identity.application.service.UserMapper;
import com.zhanlin.task_tracker.identity.infrastructure.persistence.UserRepository;
import com.zhanlin.task_tracker.identity.infrastructure.security.JWTService;
import com.zhanlin.task_tracker.security.user.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jWTService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_shouldRegisterUserSuccessfully() {
        RegisterUserRequest request = new RegisterUserRequest(
                "test@example.com",
                "password",
                "password"
        );

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(false);

        when(userMapper.toEntity(request))
                .thenReturn(user);

        when(passwordEncoder.encode("password"))
                .thenReturn("encoded-password");

        when(userRepository.save(user))
                .thenReturn(user);

        when(jWTService.generateToken(any(CustomUserDetails.class)))
                .thenReturn("jwt-token");

        AuthResponse result = authService.register(request);

        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo("jwt-token");

        verify(userRepository).existsByEmail("test@example.com");
        verify(userMapper).toEntity(request);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
        verify(jWTService).generateToken(any(CustomUserDetails.class));
    }

    @Test
    void register_shouldThrowExceptionWhenPasswordsDoNotMatch() {
        RegisterUserRequest request = new RegisterUserRequest(
                "test@example.com",
                "password",
                "different-password"
        );

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(PasswordMismatchException.class);

        verifyNoInteractions(userRepository, userMapper, passwordEncoder, jWTService);
    }

    @Test
    void register_shouldThrowExceptionWhenUserAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest(
                "test@example.com",
                "password",
                "password"
        );

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistException.class);

        verify(userRepository).existsByEmail("test@example.com");
        verify(userMapper, never()).toEntity(any());
        verify(passwordEncoder, never()).encode(any());
        verify(jWTService, never()).generateToken(any());
    }

    @Test
    void login_shouldReturnTokenSuccessfully() {
        LoginRequest request = new LoginRequest(
                "test@example.com",
                "password"
        );

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");

        CustomUserDetails userDetails =
                new CustomUserDetails(user);

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(jWTService.generateToken(userDetails))
                .thenReturn("jwt-token");

        AuthResponse result = authService.login(request);

        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo("jwt-token");

        verify(authenticationManager).authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ));

        verify(jWTService).generateToken(userDetails);
    }

    @Test
    void login_shouldThrowExceptionWhenCredentialsAreInvalid() {
        LoginRequest request = new LoginRequest(
                "test@example.com",
                "wrong-password"
        );

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);

        verify(authenticationManager).authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ));

        verify(jWTService, never()).generateToken(any());
    }
}
