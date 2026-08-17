package com.zhanlin.task_tracker.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanlin.task_tracker.dto.AuthAndLoginDTO.AuthResponse;
import com.zhanlin.task_tracker.dto.AuthAndLoginDTO.LoginRequest;
import com.zhanlin.task_tracker.dto.UserDTO.RegisterUserRequest;
import com.zhanlin.task_tracker.security.service.CustomUserDetailService;
import com.zhanlin.task_tracker.security.service.JWTService;
import com.zhanlin.task_tracker.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.databind.json.JsonMapper;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    void register_shouldReturnCreated() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest(
                "test@example.com",
                "password",
                "password"
        );

        AuthResponse response = new AuthResponse("test-jwt");

        JsonMapper jsonMapper = JsonMapper.builder().build();

        when(authService.register(any(RegisterUserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("test-jwt"));
        verify(authService).register(any(RegisterUserRequest.class));
    }

    @Test
    void login_shouldReturnOk() throws Exception {

        LoginRequest request = new LoginRequest(
                "test@example.com",
                "password"
        );

        AuthResponse response = new AuthResponse("test-jwt");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        JsonMapper jsonMapper = JsonMapper.builder().build();

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void register_shouldReturnBadRequest_whenEmailInvalid() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest(
                "invalid-email",
                "password",
                "password"
        );

        JsonMapper jsonMapper = JsonMapper.builder().build();

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterUserRequest.class));
    }

    @Test
    void login_shouldReturnBadRequest_whenEmailInvalid() throws Exception {

        LoginRequest request = new LoginRequest(
                "invalid-email",
                "password"
        );

        JsonMapper jsonMapper = JsonMapper.builder().build();

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }



}
