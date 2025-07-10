package com.haykal.taskmanagementapi.controller;

import com.haykal.taskmanagementapi.dto.TaskDto;
import com.haykal.taskmanagementapi.dto.TaskRequestDto;
import com.haykal.taskmanagementapi.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "Operations related to task management")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve all tasks in the system")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieve a specific task by its ID")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Integer id) {
        Optional<TaskDto> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get tasks by user ID", description = "Retrieve all tasks for a specific user")
    public ResponseEntity<List<TaskDto>> getTasksByUserId(@PathVariable Integer userId) {
        List<TaskDto> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    @Operation(summary = "Create new task", description = "Create a new task for the authenticated user")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskRequestDto taskRequestDto,
                                        Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            TaskDto createdTask = taskService.createTask(taskRequestDto, userEmail);
            return ResponseEntity.ok(createdTask);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Update an existing task (only owner can update)")
    public ResponseEntity<?> updateTask(@PathVariable Integer id,
                                        @Valid @RequestBody TaskRequestDto taskRequestDto,
                                        Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            TaskDto updatedTask = taskService.updateTask(id, taskRequestDto, userEmail);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Delete a task (only owner can delete)")
    public ResponseEntity<?> deleteTask(@PathVariable Integer id, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            taskService.deleteTask(id, userEmail);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}