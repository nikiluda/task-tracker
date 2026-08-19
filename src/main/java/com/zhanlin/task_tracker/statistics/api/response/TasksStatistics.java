package com.zhanlin.task_tracker.statistics.api.response;

public record TasksStatistics(
        long total,
        long waiting,
        long done
) {
}
