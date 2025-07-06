package org.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.project.dto.ProjectRequestDto;
import org.project.dto.ProjectResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Project Management", description = "Operations for managing project")
public interface ProjectApi {

    @Operation(summary = "Get all Project", description = "Retrieves a list of all available Projects")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved categories",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectResponseDto.class))))
    ResponseEntity<List<ProjectResponseDto>> getAll();


    @Operation(summary = "Get project by ID", description = "Retrieves a specific project by its unique identifier")
    @Parameter(name = "id", description = "ID of the category to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the category",
            content = @Content(schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Project not found")
    ResponseEntity<ProjectResponseDto> getById(@PathVariable Long id);


    @Operation(summary = "Create new project", description = "Creates a new project category.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category details", required = true,
            content = @Content(schema = @Schema(implementation = ProjectRequestDto.class)))
    @ApiResponse(responseCode = "201", description = "Successfully created the category",
            content = @Content(schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN privileges"
    )
    ResponseEntity<ProjectResponseDto> create(@RequestBody ProjectRequestDto categoryDto);


    @Operation(summary = "Update project",
            description = "Updates the name, field... of an existing project.")
    @Parameter(name = "id", description = "ID of the category to update", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @Parameter(name = "name", description = "New category name", required = true,
            schema = @Schema(type = "string", example = "Pots and planters"))
    @ApiResponse(responseCode = "200", description = "Successfully updated the category",
            content = @Content(schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN privileges")
    @ApiResponse(responseCode = "404", description = "Project not found")
    ResponseEntity<ProjectResponseDto> update(@PathVariable(name = "id") Long id, @RequestBody ProjectRequestDto requestDto);


    @Operation(summary = "Delete project", description = "Deletes a project by its ID.")
    @Parameter(name = "id", description = "ID of the category to delete", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponse(responseCode = "200", description = "Successfully deleted the category")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN privileges")
    @ApiResponse(responseCode = "404", description = "Project not found")
    ResponseEntity<Void> delete(@PathVariable Long id);
}

