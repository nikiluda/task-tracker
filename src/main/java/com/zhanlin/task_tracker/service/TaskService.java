package com.zhanlin.task_tracker.service;

import com.zhanlin.task_tracker.dto.TaskDTO.AssigneeRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.StatusRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskResponse;
import com.zhanlin.task_tracker.entity.Task;
import com.zhanlin.task_tracker.entity.TaskStatus;
import com.zhanlin.task_tracker.entity.User;
import com.zhanlin.task_tracker.mapper.TaskMapper;
import com.zhanlin.task_tracker.repository.TaskRepository;
import com.zhanlin.task_tracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

//Сделать наследование от интерфейса и так же добавить кастомные исключения


@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;

    public TaskService(
            TaskRepository taskRepository,
            TaskMapper taskMapper,
            UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
    }

    public TaskResponse createTask(TaskRequest request) {
        User owner = getCurrentUser();

        Task task = taskMapper.toEntity(request, owner);

        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        User owner = getCurrentUser();

        return taskRepository.findAllByOwnerId(owner.getId())
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getOneTask(Long taskId) {
        User owner = getCurrentUser();

        Task task = findTaskByIdAndOwnerId(taskId, owner.getId());

        return taskMapper.toResponse(task);
    }

    public TaskResponse updateTask(Long taskId, TaskRequest request
    ) {
        User owner = getCurrentUser();

        Task task = findTaskByIdAndOwnerId(taskId, owner.getId());

        task.setTitle(request.title());
        task.setDescription(request.description());

        return taskMapper.toResponse(task);
    }

    public TaskResponse updateStatus(Long taskId, StatusRequest request
    ) {
        User owner = getCurrentUser();

        Task task = findTaskByIdAndOwnerId(taskId, owner.getId());

        TaskStatus newStatus = request.status()
                ? TaskStatus.DONE
                : TaskStatus.WAITING;

        task.setStatus(newStatus);

        if (newStatus == TaskStatus.DONE) {
            task.setDoneAt(Instant.now());
        } else {
            task.setDoneAt(null);
        }

        return taskMapper.toResponse(task);
    }

    public TaskResponse changeAssignee(Long taskId, AssigneeRequest request
    ) {
        User owner = getCurrentUser();

        Task task = findTaskByIdAndOwnerId(taskId, owner.getId());

        User assignee = userRepository.findById(request.assigneeId())
                .orElseThrow(() ->
                        new RuntimeException("Assignee not found")
                );

        task.setAssignee(assignee);

        return taskMapper.toResponse(task);
    }

    public void deleteTask(Long taskId) {
        User owner = getCurrentUser();

        Task task = findTaskByIdAndOwnerId(taskId, owner.getId());

        taskRepository.delete(task);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + email
                        )
                );
    }

    private Task findTaskByIdAndOwnerId(Long taskId, Long ownerId) {
        return taskRepository.findByIdAndOwnerId(taskId, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found")
                );
    }
}
