package com.haykal.taskmanagementapi.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateUserRequestDto(

        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull LocalDate dateOfBirth
        ) {
}
