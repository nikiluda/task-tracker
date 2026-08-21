package com.zhanlin.task_tracker.service;

import com.zhanlin.task_tracker.dto.TaskDTO.AssigneeRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.StatusRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskResponse;
import com.zhanlin.task_tracker.entity.Task;
import com.zhanlin.task_tracker.entity.TaskStatus;
import com.zhanlin.task_tracker.identity.domain.User;
import com.zhanlin.task_tracker.exception.AssigneeNotFoundException;
import com.zhanlin.task_tracker.exception.TaskNotFoundException;
import com.zhanlin.task_tracker.mapper.TaskMapper;
import com.zhanlin.task_tracker.repository.TaskRepository;
import com.zhanlin.task_tracker.identity.infrastructure.persistence.UserRepository;
import com.zhanlin.task_tracker.specification.TaskSpecification;
import com.zhanlin.task_tracker.validation.TaskSortValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;



//Сделать наследование от интерфейса и так же добавить кастомные исключения

@Service
@Transactional
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final TaskSortValidator taskSortValidator;
    private final CurrentUserService currentUserService;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            TaskMapper taskMapper,
            UserRepository userRepository, TaskSortValidator taskSortValidator, CurrentUserService currentUserService
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
        this.taskSortValidator = taskSortValidator;
        this.currentUserService = currentUserService;
    }
    @Override
    public TaskResponse createTask(TaskRequest request) {
        User owner = currentUserService.getCurrentUser();

        Task task = taskMapper.toEntity(request, owner);

        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TaskResponse> getAllTasks(Pageable pageable, TaskStatus status, String title) {
        taskSortValidator.validate(pageable);

        User owner = currentUserService.getCurrentUser();

        Specification<Task> specification =
                TaskSpecification.belongsToOwner(owner.getId());

        if (status != null) {
            specification = specification.and(
                    TaskSpecification.hasStatus(status)
            );
        }

        if (title != null) {
            specification = specification.and(
                    TaskSpecification.titleContains(title)
            );
        }

        return taskRepository.findAll(specification, pageable)
                .map(taskMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public TaskResponse getOneTask(Long taskId) {
        User owner = currentUserService.getCurrentUser();

        Task task = findTaskByIdAndOwnerId(taskId, owner.getId());

        return taskMapper.toResponse(task);
    }

    @Override
    public TaskResponse updateTask(Long taskId, TaskRequest request
    ) {
        User owner = currentUserService.getCurrentUser();

        Task task = findTaskByIdAndOwnerId(taskId, owner.getId());

        task.setTitle(request.title());
        task.setDescription(request.description());

        return taskMapper.toResponse(task);
    }
    @Override
    public TaskResponse updateStatus(Long taskId, StatusRequest request
    ) {
        User owner = currentUserService.getCurrentUser();

        Task task = findTaskByIdAndOwnerId(taskId, owner.getId());

        TaskStatus newStatus = request.done()
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

    @Override
    public TaskResponse changeAssignee(Long taskId, AssigneeRequest request
    ) {
        User owner = currentUserService.getCurrentUser();

        Task task = findTaskByIdAndOwnerId(taskId, owner.getId());

        User assignee = userRepository.findById(request.assigneeId())
                .orElseThrow(() ->
                        new AssigneeNotFoundException()
                );

        task.setAssignee(assignee);

        return taskMapper.toResponse(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        User owner = currentUserService.getCurrentUser();

        Task task = findTaskByIdAndOwnerId(taskId, owner.getId());

        taskRepository.delete(task);
    }



    private Task findTaskByIdAndOwnerId(Long taskId, Long ownerId) {
        return taskRepository.findByIdAndOwnerId(taskId, ownerId)
                .orElseThrow(() ->
                        new TaskNotFoundException()
                );
    }
}
