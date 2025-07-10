package com.haykal.taskmanagementapi.dto;

import com.haykal.taskmanagementapi.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskDto(
        Integer id,
        @NotNull String title,
        String description,
        @NotNull LocalDateTime dueDate,
        @NotNull TaskStatus taskStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer userId
) {
}
