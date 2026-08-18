package com.zhanlin.task_tracker.service;

import com.zhanlin.task_tracker.dto.TaskDTO.AssigneeRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.StatusRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskResponse;
import com.zhanlin.task_tracker.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(TaskRequest request);
    Page<TaskResponse> getAllTasks(Pageable pageable, TaskStatus status, String title);
    TaskResponse getOneTask(Long taskId);
    TaskResponse updateTask(Long taskId, TaskRequest request);
    TaskResponse updateStatus(Long taskId, StatusRequest request);
    TaskResponse changeAssignee(Long taskId, AssigneeRequest request);
    void deleteTask(Long taskId);
}
