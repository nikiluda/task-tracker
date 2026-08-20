package com.zhanlin.task_tracker.statistics.api.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Task count statistics")
public record TasksStatistics(

        @Schema(
                description = "Total number of tasks owned by the user",
                example = "15"
        )
        long total,

        @Schema(
                description = "Number of tasks with WAITING status",
                example = "8"
        )
        long waiting,

        @Schema(
                description = "Number of tasks with DONE status",
                example = "7"
        )
        long done
) {
}