package org.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VacancyRequestDto(

        @NotBlank(message = "Name cannot be empty")
        @Size(max = 255, message = "Name cannot exceed 255 characters")
        String name,

        @Size(max = 255, message = "Field cannot exceed 255 characters")
        String field,

        @Size(max = 255, message = "Experience cannot exceed 255 characters")
        String experience,

        @Size(max=255, message ="Country cannot exceed 255 characters")
        String country,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description){
}
