package com.zhanlin.task_tracker.task.application.model;

public record TaskPageRequest(
        int page,
        int size,
        String sortBy,
        String direction
) {
}
