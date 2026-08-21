package com.zhanlin.task_tracker.task.application.service;

import com.zhanlin.task_tracker.task.api.rest.dto.AssigneeRequest;
import com.zhanlin.task_tracker.task.api.rest.dto.StatusRequest;
import com.zhanlin.task_tracker.task.api.rest.dto.TaskRequest;
import com.zhanlin.task_tracker.task.api.rest.dto.TaskResponse;
import com.zhanlin.task_tracker.task.domain.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    TaskResponse createTask(TaskRequest request);
    Page<TaskResponse> getAllTasks(Pageable pageable, TaskStatus status, String title);
    TaskResponse getOneTask(Long taskId);
    TaskResponse updateTask(Long taskId, TaskRequest request);
    TaskResponse updateStatus(Long taskId, StatusRequest request);
    TaskResponse changeAssignee(Long taskId, AssigneeRequest request);
    void deleteTask(Long taskId);
}
