package com.zhanlin.task_tracker.statistics.api.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Task completion statistics")
public record CompletionStatistics(

        @Schema(
                description = "Average task completion time in seconds",
                example = "86400"
        )
        long averageCompletionTimeSeconds
) {
}