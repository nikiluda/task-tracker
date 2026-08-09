package com.zhanlin.task_tracker.dto.TaskDTO;

import com.zhanlin.task_tracker.entity.TaskStatus;

import java.time.Instant;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Instant createdAt,
        Instant doneAt,
        Long owner,
        Long assignee
) {
}
