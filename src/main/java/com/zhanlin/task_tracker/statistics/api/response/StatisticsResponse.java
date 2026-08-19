package com.zhanlin.task_tracker.statistics.api.response;

public record StatisticsResponse(
        TasksStatistics tasks,
        CompletionStatistics completion
) {
}
