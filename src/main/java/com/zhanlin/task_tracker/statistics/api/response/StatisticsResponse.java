package com.zhanlin.task_tracker.statistics.api.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Statistics for the authenticated user's tasks")
public record StatisticsResponse(

        @Schema(description = "Task count statistics")
        TasksStatistics tasks,

        @Schema(description = "Task completion statistics")
        CompletionStatistics completion
) {
}