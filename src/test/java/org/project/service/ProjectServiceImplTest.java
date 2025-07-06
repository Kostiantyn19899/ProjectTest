package org.project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.dto.ProjectRequestDto;
import org.project.entity.Project;
import org.project.exception.ProjectAlreadyExistsException;
import org.project.exception.ProjectNotFoundException;
import org.project.repository.ProjectJpaRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectJpaRepository projectJpaRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project testProject;
    private ProjectRequestDto testProjectRequestDto;

    @BeforeEach

    void setUp() {
        testProject = Project.builder()
                .id(1L)
                .name("Test Project")
                .field("IT")
                .experience("Junior")
                .description("Description for test project")
                .deadline(LocalDate.now().plusDays(30))
                .build();

        testProjectRequestDto = new ProjectRequestDto(
                "Updated Project",
                "IT",
                "Middle",
                "Updated description",
                LocalDate.now().plusDays(60)
        );
    }

    @Test
    @DisplayName("Should retrieve all projects")
    void getAll_shouldReturnAllProjects() {
        List<Project> projects = Arrays.asList(testProject, Project.builder().id(2L).name("Project 2").build());
        when(projectJpaRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(projectJpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve project by ID when it exists")
    void getById_shouldReturnProject_whenExists() {
        when(projectJpaRepository.findById(1L)).thenReturn(Optional.of(testProject));

        Project foundProject = projectService.getById(1L);

        assertNotNull(foundProject);
        assertEquals(testProject.getId(), foundProject.getId());
        assertEquals(testProject.getName(), foundProject.getName());
        verify(projectJpaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ProjectNotFoundException when project ID does not exist")
    void getById_shouldThrowException_whenNotExists() {
        when(projectJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getById(99L));
        verify(projectJpaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should create a new project successfully")
    void create_shouldCreateProject_whenNameDoesNotExist() {
        Project newProject = Project.builder().name("New Project").build();
        when(projectJpaRepository.findByName(newProject.getName())).thenReturn(Optional.empty());
        when(projectJpaRepository.save(any(Project.class))).thenReturn(newProject);

        Project createdProject = projectService.create(newProject);

        assertNotNull(createdProject);
        assertEquals(newProject.getName(), createdProject.getName());
        verify(projectJpaRepository, times(1)).findByName(newProject.getName());
        verify(projectJpaRepository, times(1)).save(newProject);
    }

    @Test
    @DisplayName("Should throw ProjectAlreadyExistsException when project with same name exists")
    void create_shouldThrowException_whenNameExists() {
        when(projectJpaRepository.findByName(testProject.getName())).thenReturn(Optional.of(testProject));

        assertThrows(ProjectAlreadyExistsException.class, () -> projectService.create(testProject));
        verify(projectJpaRepository, times(1)).findByName(testProject.getName());
        verify(projectJpaRepository, never()).save(any(Project.class)); // Убеждаемся, что save не был вызван
    }

    @Test
    @DisplayName("Should update an existing project successfully")
    void update_shouldUpdateProject_whenProjectExists() {
        when(projectJpaRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectJpaRepository.save(any(Project.class))).thenReturn(testProject); // save вернет обновленный объект

        Project updatedProject = projectService.update(1L, testProjectRequestDto);

        assertNotNull(updatedProject);
        assertEquals(testProjectRequestDto.name(), updatedProject.getName());
        assertEquals(testProjectRequestDto.field(), updatedProject.getField());
        assertEquals(testProjectRequestDto.description(), updatedProject.getDescription());
        assertEquals(testProjectRequestDto.deadline(), updatedProject.getDeadline());

        verify(projectJpaRepository, times(1)).findById(1L);
        verify(projectJpaRepository, times(1)).save(testProject); // Убеждаемся, что save был вызван
    }

    @Test
    @DisplayName("Should throw ProjectNotFoundException when updating a non-existing project")
    void update_shouldThrowException_whenProjectNotExists() {
        when(projectJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.update(99L, testProjectRequestDto));
        verify(projectJpaRepository, times(1)).findById(99L);
        verify(projectJpaRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Should delete a project by ID successfully")
    void delete_shouldDeleteProject_whenExists() {
        when(projectJpaRepository.findById(1L)).thenReturn(Optional.of(testProject));
        doNothing().when(projectJpaRepository).deleteById(1L); // Для void методов используем doNothing()

        projectService.delete(1L);

        verify(projectJpaRepository, times(1)).findById(1L);
        verify(projectJpaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ProjectNotFoundException when deleting a non-existing project")
    void delete_shouldThrowException_whenNotExists() {
        when(projectJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.delete(99L));
        verify(projectJpaRepository, times(1)).findById(99L);
        verify(projectJpaRepository, never()).deleteById(anyLong());
    }
}
