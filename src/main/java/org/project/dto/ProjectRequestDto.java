package org.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ProjectRequestDto (

        @NotBlank(message = "Name must not be empty")
        @Size(min = 2, max = 255, message = "Length of the name should be between 2 and 255 symbols") // Изменено max на 255
        String name,

        @NotBlank(message = "Field must not be empty")
        @Size(min = 2, max = 255, message = "Length of the field should be between 2 and 255 symbols") // Изменено max на 255
        String field,

        @NotBlank(message = "Experience must not be empty")
        @Size(min = 2, max = 255, message = "Length of the experience should be between 2 and 255 symbols") // Изменено max на 255
        String experience,

        @NotBlank(message = "Description must not be empty")
        @Size(min = 2, max = 1000, message = "Length of the description should be between 2 and 1000 symbols") // Изменено max на 1000
        String description,

        @NotNull(message = "Deadline cannot be null")
        LocalDate deadline){
}
