package com.zhanlin.task_tracker.controller;

import com.zhanlin.task_tracker.dto.TaskDTO.AssigneeRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.StatusRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskResponse;
import com.zhanlin.task_tracker.entity.TaskStatus;
import com.zhanlin.task_tracker.security.service.CustomUserDetailService;
import com.zhanlin.task_tracker.security.service.JWTService;
import com.zhanlin.task_tracker.service.TaskService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    private final TaskResponse sampleTask = new TaskResponse(
            1L,
            "Sample",
            "Description",
            TaskStatus.WAITING,
            Instant.parse("2026-08-17T10:00:00Z"),
            null,
            1L,
            2L
    );

    @Test
    void getAllTasks_shouldReturnOk() throws Exception {

        Page<TaskResponse> page =
                new PageImpl<>(List.of(sampleTask));

        when(taskService.getAllTasks(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Sample"))
                .andExpect(jsonPath("$.content[0].description").value("Description"))
                .andExpect(jsonPath("$.content[0].status").value("WAITING"));

        verify(taskService).getAllTasks(any(Pageable.class));
    }

    @Test
    void getOneTask_shouldReturnOk() throws Exception {

        when(taskService.getOneTask(1L))
                .thenReturn(sampleTask);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.status").value("WAITING"));

        verify(taskService).getOneTask(1L);
    }

    @Test
    void createTask_shouldReturnCreated() throws Exception {

        TaskRequest request = new TaskRequest(
                "Sample",
                "Description"
        );

        when(taskService.createTask(any(TaskRequest.class)))
                .thenReturn(sampleTask);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.status").value("WAITING"));

        verify(taskService).createTask(any(TaskRequest.class));
    }

    @Test
    void updateTask_shouldReturnOk() throws Exception {

        TaskRequest request = new TaskRequest(
                "Updated title",
                "Updated description"
        );

        TaskResponse updatedTask = new TaskResponse(
                1L,
                "Updated title",
                "Updated description",
                TaskStatus.WAITING,
                sampleTask.createdAt(),
                null,
                1L,
                2L
        );

        when(taskService.updateTask(
                eq(1L),
                any(TaskRequest.class)
        )).thenReturn(updatedTask);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        verify(taskService)
                .updateTask(eq(1L), any(TaskRequest.class));
    }

    @Test
    void changeStatus_shouldReturnOk() throws Exception {

        StatusRequest request = new StatusRequest(true);

        TaskResponse doneTask = new TaskResponse(
                1L,
                "Sample",
                "Description",
                TaskStatus.DONE,
                sampleTask.createdAt(),
                Instant.parse("2026-08-17T12:00:00Z"),
                1L,
                2L
        );

        when(taskService.updateStatus(
                eq(1L),
                any(StatusRequest.class)
        )).thenReturn(doneTask);

        mockMvc.perform(patch("/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("DONE"));

        verify(taskService)
                .updateStatus(eq(1L), any(StatusRequest.class));
    }

    @Test
    void changeAssignee_shouldReturnOk() throws Exception {

        AssigneeRequest request = new AssigneeRequest(2L);

        when(taskService.changeAssignee(
                eq(1L),
                any(AssigneeRequest.class)
        )).thenReturn(sampleTask);

        mockMvc.perform(patch("/tasks/1/assignee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.assignee").value(2));

        verify(taskService)
                .changeAssignee(eq(1L), any(AssigneeRequest.class));
    }

    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {

        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }

    @Test
    void createTask_shouldReturnBadRequest_whenTitleBlank() throws Exception {

        TaskRequest request = new TaskRequest(
                "",
                "Description"
        );

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(taskService, never())
                .createTask(any(TaskRequest.class));
    }

    @Test
    void updateTask_shouldReturnBadRequest_whenTitleBlank() throws Exception {

        TaskRequest request = new TaskRequest(
                "",
                "Description"
        );

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(taskService, never())
                .updateTask(anyLong(), any(TaskRequest.class));
    }

    @Test
    void changeAssignee_shouldReturnBadRequest_whenAssigneeIdNull() throws Exception {

        AssigneeRequest request = new AssigneeRequest(null);

        mockMvc.perform(patch("/tasks/1/assignee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(taskService, never())
                .changeAssignee(anyLong(), any(AssigneeRequest.class));
    }
}