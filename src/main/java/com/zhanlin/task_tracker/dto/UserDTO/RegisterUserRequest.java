package com.zhanlin.task_tracker.dto.UserDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email must not be blank")
        String email,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
        String password,

        @NotBlank(message = "Password confirmation must not be blank")
        String confirmPassword
) {
}
