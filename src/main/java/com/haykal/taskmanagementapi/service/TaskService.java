package com.haykal.taskmanagementapi.service;

import com.haykal.taskmanagementapi.dto.TaskDto;
import com.haykal.taskmanagementapi.dto.TaskRequestDto;
import com.haykal.taskmanagementapi.entity.Task;
import com.haykal.taskmanagementapi.entity.User;
import com.haykal.taskmanagementapi.repository.TaskRepository;
import com.haykal.taskmanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskDto createTask(TaskRequestDto taskRequestDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = Task.builder()
                .title(taskRequestDto.title())
                .description(taskRequestDto.description())
                .dueDate(taskRequestDto.dueDate())
                .status(taskRequestDto.taskStatus())
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Task savedTask = taskRepository.save(task);
        return convertToDto(savedTask);
    }

    public Optional<TaskDto> getTaskById(Integer id) {
        return taskRepository.findById(id)
                .map(this::convertToDto);
    }

    public TaskDto updateTask(Integer id, TaskRequestDto taskRequestDto, String userEmail) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!existingTask.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only update your own tasks");
        }

        existingTask.setTitle(taskRequestDto.title());
        existingTask.setDescription(taskRequestDto.description());
        existingTask.setDueDate(taskRequestDto.dueDate());
        existingTask.setStatus(taskRequestDto.taskStatus());
        existingTask.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(existingTask);
        return convertToDto(updatedTask);
    }

    public void deleteTask(Integer id, String userEmail) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only delete your own tasks");
        }

        taskRepository.deleteById(id);
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByUserId(Integer userId) {
        return taskRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TaskDto convertToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getUser().getId()
        );
    }
}