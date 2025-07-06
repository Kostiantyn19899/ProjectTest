package org.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.project.dto.VacancyRequestDto;
import org.project.dto.VacancyResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Tag(name = "Vacancy Management", description = "Operations for managing vacancies")
public interface VacancyApi {

    @Operation(summary = "Get vacancies by Project ID", description = "Retrieves a list of vacancies for a specific project")
    @Parameter(name = "id", description = "ID of the project to retrieve vacancies for", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vacancies",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = VacancyResponseDto.class))))
    @ApiResponse(responseCode = "404", description = "Project not found")
    ResponseEntity<List<VacancyResponseDto>> getVacanciesByProjectId(@PathVariable Long id);

    @Operation(summary = "Add new vacancy to project", description = "Adds a new vacancy to a specific project.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Vacancy details", required = true,
            content = @Content(schema = @Schema(implementation = VacancyRequestDto.class)))
    @ApiResponse(responseCode = "201", description = "Successfully created the vacancy",
            content = @Content(schema = @Schema(implementation = VacancyResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN privileges")
    @ApiResponse(responseCode = "404", description = "Project not found")
    ResponseEntity<VacancyResponseDto> create(@Valid @PathVariable(name = "id") Long id, @RequestBody VacancyRequestDto requestDto);

    @Operation(summary = "Update vacancy",
            description = "Updates the name, field, experience, country, and description of an existing vacancy.")
    @Parameter(name = "id", description = "ID of the vacancy to update", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponse(responseCode = "200", description = "Successfully updated the vacancy",
            content = @Content(schema = @Schema(implementation = VacancyResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN privileges")
    @ApiResponse(responseCode = "404", description = "Vacancy not found")
    ResponseEntity<VacancyResponseDto> updateVacancy(@PathVariable Long id, @Valid @RequestBody VacancyRequestDto request);

    @Operation(summary = "Delete vacancy", description = "Deletes a vacancy by its ID.")
    @Parameter(name = "id", description = "ID of the vacancy to delete", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponse(responseCode = "200", description = "Successfully deleted the vacancy")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN privileges")
    @ApiResponse(responseCode = "404", description = "Vacancy not found")
    ResponseEntity<Void> deleteVacancy(@PathVariable Long id);
}

