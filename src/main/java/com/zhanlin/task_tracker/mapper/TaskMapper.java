package com.zhanlin.task_tracker.mapper;

import com.zhanlin.task_tracker.dto.TaskDTO.CreateTaskRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskResponse;
import com.zhanlin.task_tracker.entity.Task;
import com.zhanlin.task_tracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(CreateTaskRequest request, User assignee) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setAssignee(assignee);
        return task;
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),task.getCreatedAt(),task.getDoneAt(),task.getOwner().getId(),task.getAssignee().getId());
    }
}
