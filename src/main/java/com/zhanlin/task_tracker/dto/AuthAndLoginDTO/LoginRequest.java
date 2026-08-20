package com.zhanlin.task_tracker.dto.AuthAndLoginDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User login credentials")
public record LoginRequest(

        @Schema(
                description = "User email address",
                example = "user@example.com"
        )
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email must not be blank")
        String email,

        @Schema(
                description = "User password",
                example = "password123",
                format = "password"
        )
        @NotBlank(message = "Password must not be blank")
        String password
) {
}