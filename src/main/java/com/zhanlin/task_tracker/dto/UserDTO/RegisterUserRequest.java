package com.zhanlin.task_tracker.dto.UserDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 4)
        String password,
        @NotBlank
        String confirmPassword
) {
}
