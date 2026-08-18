package com.zhanlin.task_tracker.service;

import com.zhanlin.task_tracker.dto.TaskDTO.AssigneeRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.StatusRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskResponse;
import com.zhanlin.task_tracker.entity.Task;
import com.zhanlin.task_tracker.entity.TaskStatus;
import com.zhanlin.task_tracker.entity.User;
import com.zhanlin.task_tracker.exception.TaskNotFoundException;
import com.zhanlin.task_tracker.mapper.TaskMapper;
import com.zhanlin.task_tracker.repository.TaskRepository;
import com.zhanlin.task_tracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        task = new Task();
        task.setId(1L);
        task.setTitle("Тестовая задача");
        task.setDescription("Описание тестовой задачи");
        task.setOwner(user);
        task.setStatus(TaskStatus.WAITING);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createTask_shouldCreateTaskSuccessfully() {

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "test@example.com",
                                null
                        )
                );

        TaskRequest request = new TaskRequest(
                "Тестовая задача",
                "Описание тестовой задачи"
        );

        when(taskMapper.toEntity(request, user))
                .thenReturn(task);

        when(taskRepository.save(task))
                .thenReturn(task);

        TaskResponse response = new TaskResponse(
                1L,
                "Тестовая задача",
                "Описание тестовой задачи",
                TaskStatus.WAITING,
                task.getCreatedAt(),
                null,
                1L,
                null
        );

        when(taskMapper.toResponse(task))
                .thenReturn(response);

        TaskResponse result = taskService.createTask(request);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Тестовая задача");
        assertThat(result.description()).isEqualTo("Описание тестовой задачи");
        assertThat(result.status()).isEqualTo(TaskStatus.WAITING);

        verify(userRepository).findByEmail("test@example.com");
        verify(taskRepository).save(task);
        verify(taskMapper).toResponse(task);
        verify(taskMapper).toEntity(request, user);
    }

    @Test
    void getAllTasks_shouldReturnOwnerTasks() {

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "test@example.com",
                                null
                        )
                );

        Pageable pageable = PageRequest.of(0, 10);

        Page<Task> taskPage = new PageImpl<>(
                List.of(task),
                pageable,
                1
        );

        when(taskRepository.findAllByOwnerId(1L, pageable))
                .thenReturn(taskPage);

        TaskResponse response = new TaskResponse(
                1L,
                "Тестовая задача",
                "Описание тестовой задачи",
                TaskStatus.WAITING,
                task.getCreatedAt(),
                null,
                1L,
                null
        );

        when(taskMapper.toResponse(task))
                .thenReturn(response);

        Page<TaskResponse> result = taskService.getAllTasks(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).title())
                .isEqualTo("Тестовая задача");
        assertThat(result.getContent().get(0).description())
                .isEqualTo("Описание тестовой задачи");
        assertThat(result.getContent().get(0).status())
                .isEqualTo(TaskStatus.WAITING);

        assertThat(result.getTotalElements())
                .isEqualTo(1);

        assertThat(result.getTotalPages())
                .isEqualTo(1);

        verify(userRepository)
                .findByEmail("test@example.com");

        verify(taskRepository)
                .findAllByOwnerId(1L, pageable);

        verify(taskMapper)
                .toResponse(task);
    }

    @Test
    void updateStatus_shouldSetDone() {

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "test@example.com",
                                null
                        )
                );

        when(taskRepository.findByIdAndOwnerId(1L, 1L))
                .thenReturn(Optional.of(task));

        when(taskMapper.toResponse(task))
                .thenAnswer(invocation -> {
                    Task t = invocation.getArgument(0);

                    return new TaskResponse(
                            t.getId(),
                            t.getTitle(),
                            t.getDescription(),
                            t.getStatus(),
                            t.getCreatedAt(),
                            t.getDoneAt(),
                            t.getOwner().getId(),
                            null
                    );
                });

        StatusRequest request = new StatusRequest(true);

        TaskResponse result = taskService.updateStatus(1L, request);

        assertThat(result.status())
                .isEqualTo(TaskStatus.DONE);

        assertThat(result.doneAt())
                .isNotNull();

        verify(taskRepository)
                .findByIdAndOwnerId(1L, 1L);

        verify(taskMapper)
                .toResponse(task);
    }

    @Test
    void updateStatus_shouldSetWaitingAndClearDoneAt() {

        task.setStatus(TaskStatus.DONE);
        task.setDoneAt(Instant.now());

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "test@example.com",
                                null
                        )
                );

        when(taskRepository.findByIdAndOwnerId(1L, 1L))
                .thenReturn(Optional.of(task));

        when(taskMapper.toResponse(task))
                .thenAnswer(invocation -> {
                    Task t = invocation.getArgument(0);

                    return new TaskResponse(
                            t.getId(),
                            t.getTitle(),
                            t.getDescription(),
                            t.getStatus(),
                            t.getCreatedAt(),
                            t.getDoneAt(),
                            t.getOwner().getId(),
                            null
                    );
                });

        StatusRequest request = new StatusRequest(false);

        TaskResponse result = taskService.updateStatus(1L, request);

        assertThat(result.status())
                .isEqualTo(TaskStatus.WAITING);

        assertThat(result.doneAt())
                .isNull();

        verify(taskRepository)
                .findByIdAndOwnerId(1L, 1L);

        verify(taskMapper)
                .toResponse(task);
    }

    @Test
    void getOneTask_shouldThrowExceptionTaskNotFound() {

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "test@example.com",
                                null
                        )
                );

        when(taskRepository.findByIdAndOwnerId(99L, 1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getOneTask(99L))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository)
                .findByIdAndOwnerId(99L, 1L);
    }

    @Test
    void deleteTask_shouldDeleteTask() {

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "test@example.com",
                                null
                        )
                );

        when(taskRepository.findByIdAndOwnerId(1L, 1L))
                .thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository)
                .findByIdAndOwnerId(1L, 1L);

        verify(taskRepository)
                .delete(task);
    }

    @Test
    void getOneTask_shouldReturnTask() {

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "test@example.com",
                                null
                        )
                );

        when(taskRepository.findByIdAndOwnerId(1L, 1L))
                .thenReturn(Optional.of(task));

        TaskResponse response = new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getDoneAt(),
                user.getId(),
                null
        );

        when(taskMapper.toResponse(task))
                .thenReturn(response);

        TaskResponse result = taskService.getOneTask(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Тестовая задача");
        assertThat(result.status()).isEqualTo(TaskStatus.WAITING);

        verify(taskRepository)
                .findByIdAndOwnerId(1L, 1L);

        verify(taskMapper)
                .toResponse(task);
    }

    @Test
    void updateTask_shouldUpdateTaskSuccessfully() {

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "test@example.com",
                                null
                        )
                );

        when(taskRepository.findByIdAndOwnerId(1L, 1L))
                .thenReturn(Optional.of(task));

        TaskRequest request = new TaskRequest(
                "Обновленная задача",
                "Обновленное описание"
        );

        when(taskMapper.toResponse(task))
                .thenAnswer(invocation -> {
                    Task t = invocation.getArgument(0);

                    return new TaskResponse(
                            t.getId(),
                            t.getTitle(),
                            t.getDescription(),
                            t.getStatus(),
                            t.getCreatedAt(),
                            t.getDoneAt(),
                            t.getOwner().getId(),
                            null
                    );
                });

        TaskResponse result = taskService.updateTask(1L, request);

        assertThat(result).isNotNull();
        assertThat(result.title())
                .isEqualTo("Обновленная задача");
        assertThat(result.description())
                .isEqualTo("Обновленное описание");
        assertThat(result.status())
                .isEqualTo(TaskStatus.WAITING);

        verify(userRepository)
                .findByEmail("test@example.com");

        verify(taskRepository)
                .findByIdAndOwnerId(1L, 1L);

        verify(taskMapper)
                .toResponse(task);
    }

    @Test
    void changeAssignee_shouldChangeAssigneeSuccessfully() {

        User assignee = new User();
        assignee.setId(2L);
        assignee.setEmail("assignee@example.com");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "test@example.com",
                                null
                        )
                );

        when(taskRepository.findByIdAndOwnerId(1L, 1L))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(assignee));

        TaskResponse response = new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getDoneAt(),
                user.getId(),
                assignee.getId()
        );

        when(taskMapper.toResponse(task))
                .thenReturn(response);

        AssigneeRequest request = new AssigneeRequest(2L);

        TaskResponse result = taskService.changeAssignee(1L, request);

        assertThat(result).isNotNull();
        assertThat(result.assignee())
                .isEqualTo(2L);

        assertThat(task.getAssignee())
                .isEqualTo(assignee);

        verify(userRepository)
                .findByEmail("test@example.com");

        verify(taskRepository)
                .findByIdAndOwnerId(1L, 1L);

        verify(userRepository)
                .findById(2L);

        verify(taskMapper)
                .toResponse(task);
    }
}