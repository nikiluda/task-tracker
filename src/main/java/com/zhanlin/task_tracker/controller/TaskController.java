package com.zhanlin.task_tracker.controller;

import com.zhanlin.task_tracker.dto.TaskDTO.AssigneeRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.StatusRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskRequest;
import com.zhanlin.task_tracker.dto.TaskDTO.TaskResponse;
import com.zhanlin.task_tracker.entity.TaskStatus;
import com.zhanlin.task_tracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Tasks",
    description = "Operations for managing user tasks")
@RestController
@RequestMapping("/tasks")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Get tasks",
            description = "Returns tasks belonging to the authenticated user with optional filtering and pagination"
    )
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(@ParameterObject Pageable pageable, @RequestParam(required = false) TaskStatus status,
                                                          @RequestParam(required = false) String title) {
        return ResponseEntity.ok(
                taskService.getAllTasks(pageable, status, title)
        );
    }

    @Operation(
            summary = "Get task",
            description = "Returns a task belonging to the authenticated user"
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getOneTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getOneTask(id));
    }

    @Operation(
            summary = "Create task",
            description = "Creates a new task for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Task successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid task data"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            )
    })
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(request));
    }

    @Operation(
            summary = "Update task",
            description = "Updates a task belonging to the authenticated user"
    )
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody @Valid TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @Operation(
            summary = "Change task status",
            description = "Changes the status of a task owned by the authenticated user"
    )
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> changeStatus(@PathVariable Long id, @RequestBody @Valid StatusRequest request) {
        return ResponseEntity.ok(taskService.updateStatus(id, request));
    }

    @Operation(
            summary = "Change task assignee",
            description = "Changes the assignee of a task owned by the authenticated user"

    )
    @PatchMapping("/{id}/assignee")
    public ResponseEntity<TaskResponse> changeAssignee(@PathVariable Long id, @RequestBody @Valid AssigneeRequest request) {
        return ResponseEntity.ok(taskService.changeAssignee(id, request));
    }

    @Operation(
            summary = "Delete task",
            description = "Deletes a task belonging to the authenticated user"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }


}
