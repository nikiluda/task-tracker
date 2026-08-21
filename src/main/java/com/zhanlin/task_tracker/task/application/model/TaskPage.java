package com.zhanlin.task_tracker.task.application.model;

import com.zhanlin.task_tracker.task.domain.Task;

import java.util.List;

public record TaskPage(
        List<Task> tasks,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
