package com.zhanlin.task_tracker.service;

import com.zhanlin.task_tracker.dto.TaskDTO.AssigneeRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.StatusRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskResponse;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(TaskRequest request);
    List<TaskResponse> getAllTasks();
    TaskResponse getOneTask(Long taskId);
    TaskResponse updateTask(Long taskId, TaskRequest request);
    TaskResponse updateStatus(Long taskId, StatusRequest request);
    TaskResponse changeAssignee(Long taskId, AssigneeRequest request);
    void deleteTask(Long taskId);
}
