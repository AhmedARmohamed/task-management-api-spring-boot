package com.haykal.taskmanagementapi.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterRequest(
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String email,
        @NotNull String password,
        @NotNull LocalDate dateOfBirth
) {
}
