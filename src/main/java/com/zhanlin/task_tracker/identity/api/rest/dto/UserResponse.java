package com.zhanlin.task_tracker.identity.api.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authenticated user information")
public record UserResponse(

        @Schema(
                description = "Unique user identifier",
                example = "1"
        )
        Long id,

        @Schema(
                description = "User email address",
                example = "user@example.com"
        )
        String email
) {
}