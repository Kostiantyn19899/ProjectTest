package org.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProjectRequestDto (

        @NotBlank(message = "Name must not be empty")
        @Size(min = 2, max = 50, message = "Length of the name should be between 2 and 50 symbols")
        String name,

        @NotBlank(message = "field must not be empty")
        @Size(min = 2, max = 50, message = "Length of the name should be between 2 and 50 symbols")
        String field,

        @NotBlank(message = "experience must not be empty")
        @Size(min = 2, max = 50, message = "Length of the name should be between 2 and 50 symbols")
        String experience,

        @NotBlank(message = "Description must not be empty")
        @Size(min = 2, max = 500, message = "Length of the description should be between 2 and 500 symbols")
        String description,

        @NotNull(message = "Deadline cannot be null")
        LocalDate deadline){
}
