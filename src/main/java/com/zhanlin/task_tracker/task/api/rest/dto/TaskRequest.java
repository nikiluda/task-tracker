package com.zhanlin.task_tracker.task.api.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request for creating or updating a task")
public record TaskRequest(

        @Schema(
                description = "Task title",
                example = "Complete Spring Security integration",
                maxLength = 200,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Title must not be blank")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @Schema(
                description = "Detailed task description",
                example = "Implement JWT authentication and configure protected endpoints",
                maxLength = 2000
        )
        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        String description
) {
}