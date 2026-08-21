package com.zhanlin.task_tracker.identity.api.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing a JWT access token")
public record AuthResponse(

        @Schema(
                description = "JWT access token used to authenticate protected API requests",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIn0.example-signature"
        )
        String token
) {
}