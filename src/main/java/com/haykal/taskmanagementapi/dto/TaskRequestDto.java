package com.haykal.taskmanagementapi.dto;

import com.haykal.taskmanagementapi.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskRequestDto(
        @NotNull String title,
        @NotNull String description,
        @NotNull LocalDateTime dueDate,
        @NotNull TaskStatus taskStatus
) {
}
