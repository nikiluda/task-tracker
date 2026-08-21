package com.zhanlin.task_tracker.task.application.model;

import com.zhanlin.task_tracker.task.domain.TaskStatus;

public record TaskSearchCriteria(
        Long ownerId,
        TaskStatus status,
        String title
) {
}
