package com.zhanlin.task_tracker.dto.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request for registering a new user")
public record RegisterUserRequest(

        @Schema(
                description = "Unique email address of the user",
                example = "user@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email must not be blank")
        String email,

        @Schema(
                description = "User password. Must contain between 4 and 100 characters",
                example = "password123",
                format = "password",
                minLength = 4,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Password must not be blank")
        @Size(
                min = 4,
                max = 100,
                message = "Password must be between 4 and 100 characters"
        )
        String password,

        @Schema(
                description = "Password confirmation. Must match the password",
                example = "password123",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Password confirmation must not be blank")
        String confirmPassword
) {
}