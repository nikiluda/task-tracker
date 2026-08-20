package com.zhanlin.task_tracker.dto.TaskDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request for assigning a task to a user")
public record AssigneeRequest(

        @Schema(
                description = "ID of the user who will be assigned to the task",
                example = "2"
        )
        @NotNull(message = "Assignee ID must not be null")
        Long assigneeId
) {
}