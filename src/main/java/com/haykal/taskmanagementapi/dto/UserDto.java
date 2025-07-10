package com.haykal.taskmanagementapi.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserDto(
        Integer id,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String email,
        @NotNull LocalDate dateOfBirth
) {
}
