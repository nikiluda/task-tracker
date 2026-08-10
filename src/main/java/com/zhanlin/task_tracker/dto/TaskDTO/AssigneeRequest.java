package com.zhanlin.task_tracker.dto.TaskDTO;

import jakarta.validation.constraints.NotNull;

public record AssigneeRequest(
        @NotNull(message = "Assignee ID must not be null")
        Long assigneeId
) {
}
