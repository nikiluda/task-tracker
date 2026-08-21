package com.zhanlin.task_tracker.task.application.service;

import com.zhanlin.task_tracker.task.api.rest.dto.TaskRequest;
import com.zhanlin.task_tracker.task.api.rest.dto.TaskResponse;
import com.zhanlin.task_tracker.task.domain.Task;
import com.zhanlin.task_tracker.task.domain.TaskStatus;
import com.zhanlin.task_tracker.identity.domain.User;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequest request, User owner) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setOwner(owner);
        task.setStatus(TaskStatus.WAITING);
        return task;
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getDoneAt(),
                task.getOwner().getId(),
                task.getAssignee() != null
                        ? task.getAssignee().getId()
                        : null
        );
    }
}
