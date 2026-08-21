package com.zhanlin.task_tracker.task.api.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request for changing the completion status of a task")
public record StatusRequest(

        @Schema(
                description = "Whether the task should be marked as completed",
                example = "true"
        )
        boolean done
) {
}