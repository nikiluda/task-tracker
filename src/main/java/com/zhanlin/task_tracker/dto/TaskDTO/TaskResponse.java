package com.zhanlin.task_tracker.dto.TaskDTO;

import com.zhanlin.task_tracker.entity.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Task information returned by the API")
public record TaskResponse(

        @Schema(
                description = "Unique task identifier",
                example = "42"
        )
        Long id,

        @Schema(
                description = "Task title",
                example = "Complete Spring Security integration"
        )
        String title,

        @Schema(
                description = "Detailed task description",
                example = "Implement JWT authentication and configure protected endpoints"
        )
        String description,

        @Schema(
                description = "Current task status",
                example = "WAITING"
        )
        TaskStatus status,

        @Schema(
                description = "Date and time when the task was created",
                example = "2026-08-20T14:30:00Z"
        )
        Instant createdAt,

        @Schema(
                description = "Date and time when the task was completed. Null if the task is not completed",
                example = "2026-08-21T10:15:00Z",
                nullable = true
        )
        Instant doneAt,

        @Schema(
                description = "ID of the user who owns the task",
                example = "1"
        )
        Long owner,

        @Schema(
                description = "ID of the user assigned to the task. Null if the task has no assignee",
                example = "2",
                nullable = true
        )
        Long assignee
) {
}