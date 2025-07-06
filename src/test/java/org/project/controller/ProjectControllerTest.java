package org.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.dto.ProjectMapper;
import org.project.dto.ProjectRequestDto;
import org.project.dto.ProjectResponseDto;
import org.project.entity.Project;
import org.project.exception.ProjectAlreadyExistsException;
import org.project.exception.ProjectNotFoundException;
import org.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // To convert DTOs to JSON

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectMapper projectMapper;

    private Project project1;
    private Project project2;
    private ProjectRequestDto projectRequestDto;
    private ProjectResponseDto projectResponseDto1;
    private ProjectResponseDto projectResponseDto2;

    @BeforeEach
    void setUp() {
        project1 = Project.builder()
                .id(1L)
                .name("Project Alpha")
                .field("IT")
                .experience("Mid")
                .description("IT project description.")
                .deadline(LocalDate.now().plusMonths(6))
                .build();

        project2 = Project.builder()
                .id(2L)
                .name("Project Beta")
                .field("Marketing")
                .experience("Senior")
                .description("Marketing project description.")
                .deadline(LocalDate.now().plusMonths(3))
                .build();

        projectRequestDto = new ProjectRequestDto(
                "New Project Name",
                "New Field",
                "New Experience",
                "New Description",
                LocalDate.now().plusMonths(1)
        );

        projectResponseDto1 = new ProjectResponseDto(
                1L,
                "Project Alpha",
                "IT",
                "Mid",
                "IT project description.",
                LocalDate.now().plusMonths(6)
        );

        projectResponseDto2 = new ProjectResponseDto(
                2L,
                "Project Beta",
                "Marketing",
                "Senior",
                "Marketing project description.",
                LocalDate.now().plusMonths(3)
        );
    }

    @Test
    @DisplayName("GET /projects should return all projects")
    void getAllProjects() throws Exception {
        List<Project> projects = Arrays.asList(project1, project2);
        List<ProjectResponseDto> dtos = Arrays.asList(projectResponseDto1, projectResponseDto2);

        when(projectService.getAll()).thenReturn(projects);
        when(projectMapper.toDto(project1)).thenReturn(projectResponseDto1);
        when(projectMapper.toDto(project2)).thenReturn(projectResponseDto2);

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(projectResponseDto1.id()))
                .andExpect(jsonPath("$[1].name").value(projectResponseDto2.name()));

        verify(projectService, times(1)).getAll();
        verify(projectMapper, times(2)).toDto(any(Project.class));
    }

    @Test
    @DisplayName("GET /projects/{id} should return project by ID")
    void getProjectById() throws Exception {
        when(projectService.getById(1L)).thenReturn(project1);
        when(projectMapper.toDto(project1)).thenReturn(projectResponseDto1);

        mockMvc.perform(get("/projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(projectResponseDto1.id()))
                .andExpect(jsonPath("$.name").value(projectResponseDto1.name()));

        verify(projectService, times(1)).getById(1L);
        verify(projectMapper, times(1)).toDto(project1);
    }

    @Test
    @DisplayName("GET /projects/{id} should return 404 for not found project")
    void getProjectById_NotFound() throws Exception {
        when(projectService.getById(anyLong())).thenThrow(new ProjectNotFoundException("Project not found"));

        mockMvc.perform(get("/projects/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Project not found"));

        verify(projectService, times(1)).getById(99L);
        verify(projectMapper, never()).toDto(any(Project.class));
    }

    @Test
    @DisplayName("POST /projects should create a new project")
    void createProject() throws Exception {
        Project newProject = Project.builder()
                .name(projectRequestDto.name())
                .field(projectRequestDto.field())
                .experience(projectRequestDto.experience())
                .description(projectRequestDto.description())
                .deadline(projectRequestDto.deadline())
                .build();
        Project createdProject = Project.builder()
                .id(3L)
                .name(projectRequestDto.name())
                .field(projectRequestDto.field())
                .experience(projectRequestDto.experience())
                .description(projectRequestDto.description())
                .deadline(projectRequestDto.deadline())
                .build();
        ProjectResponseDto createdProjectDto = new ProjectResponseDto(
                3L,
                projectRequestDto.name(),
                projectRequestDto.field(),
                projectRequestDto.experience(),
                projectRequestDto.description(),
                projectRequestDto.deadline()
        );

        when(projectMapper.toEntity(any(ProjectRequestDto.class))).thenReturn(newProject);
        when(projectService.create(any(Project.class))).thenReturn(createdProject);
        when(projectMapper.toDto(any(Project.class))).thenReturn(createdProjectDto);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdProjectDto.id()))
                .andExpect(jsonPath("$.name").value(createdProjectDto.name()));

        verify(projectMapper, times(1)).toEntity(projectRequestDto);
        verify(projectService, times(1)).create(newProject);
        verify(projectMapper, times(1)).toDto(createdProject);
    }

    @Test
    @DisplayName("POST /projects should return 400 for invalid input")
    void createProject_InvalidInput() throws Exception {
        ProjectRequestDto invalidDto = new ProjectRequestDto(
                "", // Blank name
                "Field",
                "Exp",
                "Desc",
                LocalDate.now()
        );

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name must not be empty")); // Expecting specific validation error

        verify(projectService, never()).create(any(Project.class));
    }

    @Test
    @DisplayName("POST /projects should return 409 for existing project name")
    void createProject_AlreadyExists() throws Exception {
        when(projectMapper.toEntity(any(ProjectRequestDto.class))).thenReturn(project1);
        when(projectService.create(any(Project.class))).thenThrow(new ProjectAlreadyExistsException("Project already exists"));

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Project already exists"));

        verify(projectService, times(1)).create(any(Project.class));
    }


    @Test
    @DisplayName("PUT /projects/{id} should update an existing project")
    void updateProject() throws Exception {
        Project updatedProject = Project.builder()
                .id(1L)
                .name(projectRequestDto.name())
                .field(projectRequestDto.field())
                .experience(projectRequestDto.experience())
                .description(projectRequestDto.description())
                .deadline(projectRequestDto.deadline())
                .build();
        ProjectResponseDto updatedProjectDto = new ProjectResponseDto(
                1L,
                projectRequestDto.name(),
                projectRequestDto.field(),
                projectRequestDto.experience(),
                projectRequestDto.description(),
                projectRequestDto.deadline()
        );

        when(projectService.update(eq(1L), any(ProjectRequestDto.class))).thenReturn(updatedProject);
        when(projectMapper.toDto(any(Project.class))).thenReturn(updatedProjectDto);

        mockMvc.perform(put("/projects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedProjectDto.id()))
                .andExpect(jsonPath("$.name").value(updatedProjectDto.name()));

        verify(projectService, times(1)).update(1L, projectRequestDto);
        verify(projectMapper, times(1)).toDto(updatedProject);
    }

    @Test
    @DisplayName("PUT /projects/{id} should return 404 for not found project during update")
    void updateProject_NotFound() throws Exception {
        when(projectService.update(anyLong(), any(ProjectRequestDto.class)))
                .thenThrow(new ProjectNotFoundException("Project not found"));

        mockMvc.perform(put("/projects/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Project not found"));

        verify(projectService, times(1)).update(99L, projectRequestDto);
        verify(projectMapper, never()).toDto(any(Project.class));
    }

    @Test
    @DisplayName("DELETE /projects/{id} should delete a project")
    void deleteProject() throws Exception {
        doNothing().when(projectService).delete(1L);

        mockMvc.perform(delete("/projects/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(projectService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /projects/{id} should return 404 for not found project during delete")
    void deleteProject_NotFound() throws Exception {
        doThrow(new ProjectNotFoundException("Project not found")).when(projectService).delete(anyLong());

        mockMvc.perform(delete("/projects/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Project not found"));

        verify(projectService, times(1)).delete(99L);
    }
}