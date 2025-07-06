package org.project.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.dto.VacancyRequestDto;
import org.project.entity.Project;
import org.project.entity.Vacancy;
import org.project.exception.ProjectNotFoundException;
import org.project.exception.VacancyNotFoundException;
import org.project.repository.ProjectJpaRepository;
import org.project.repository.VacancyJpaRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VacancyServiceImpl Unit Tests")
class VacancyServiceImplTest {

    @Mock
    private VacancyJpaRepository vacancyRepository;

    @Mock
    private ProjectJpaRepository projectRepository;

    @InjectMocks
    private VacancyServiceImpl vacancyService;

    private Project project;
    private Vacancy vacancy1;
    private Vacancy vacancy2;
    private VacancyRequestDto vacancyRequestDto;

    @BeforeEach
    void setUp() {
        project = Project.builder()
                .id(1L)
                .name("Project Alpha")
                .field("IT")
                .description("An IT project")
                .experience("Junior")
                .deadline(LocalDate.now().plusMonths(6))
                .build();

        vacancy1 = Vacancy.builder()
                .id(101L)
                .name("Java Developer")
                .field("Backend")
                .experience("3 years")
                .country("Germany")
                .description("Looking for a skilled Java developer.")
                .project(project)
                .build();

        vacancy2 = Vacancy.builder()
                .id(102L)
                .name("Frontend Developer")
                .field("Frontend")
                .experience("2 years")
                .country("Germany")
                .description("Seeking a React specialist.")
                .project(project)
                .build();

        vacancyRequestDto = new VacancyRequestDto(
                "Updated Vacancy Name",
                "Updated Field",
                "Updated Experience",
                "Updated Country",
                "Updated Description"
        );
    }

    @Test
    @DisplayName("Should throw ProjectNotFoundException when no vacancies found for project ID")
    void getVacanciesByProjectId_ProjectNotFound() {
        when(vacancyRepository.findByProjectId(anyLong())).thenReturn(Collections.emptyList());

        assertThrows(ProjectNotFoundException.class, () -> vacancyService.getByProjectId(99L));
        verify(vacancyRepository, times(1)).findByProjectId(99L);
    }


    @Test
    @DisplayName("Should add a vacancy to an existing project")
    void addVacancyToProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(vacancyRepository.save(any(Vacancy.class))).thenReturn(vacancy1);

        Vacancy addedVacancy = vacancyService.addVacancyToProject(vacancyRequestDto, 1L);

        assertNotNull(addedVacancy);
        assertEquals(vacancyRequestDto.name(), addedVacancy.getName());
        assertEquals(project, addedVacancy.getProject());
        verify(projectRepository, times(1)).findById(1L);
        verify(vacancyRepository, times(1)).save(any(Vacancy.class));
    }

    @Test
    @DisplayName("Should throw ProjectNotFoundException when adding vacancy to non-existent project")
    void addVacancyToProject_ProjectNotFound() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> vacancyService.addVacancyToProject(vacancyRequestDto, 99L));
        verify(projectRepository, times(1)).findById(99L);
        verify(vacancyRepository, never()).save(any(Vacancy.class));
    }

    @Test
    @DisplayName("Should update an existing vacancy")
    void updateVacancy() {
        when(vacancyRepository.findById(101L)).thenReturn(Optional.of(vacancy1));
        when(vacancyRepository.save(any(Vacancy.class))).thenReturn(vacancy1);

        Vacancy updatedVacancy = vacancyService.updateVacancy(101L, vacancyRequestDto);

        assertNotNull(updatedVacancy);
        assertEquals(vacancyRequestDto.name(), updatedVacancy.getName());
        assertEquals(vacancyRequestDto.description(), updatedVacancy.getDescription());
        verify(vacancyRepository, times(1)).findById(101L);
        verify(vacancyRepository, times(1)).save(vacancy1);
    }

    @Test
    @DisplayName("Should throw VacancyNotFoundException when updating a non-existent vacancy")
    void updateVacancy_NotFound() {
        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(VacancyNotFoundException.class, () -> vacancyService.updateVacancy(999L, vacancyRequestDto));
        verify(vacancyRepository, times(1)).findById(999L);
        verify(vacancyRepository, never()).save(any(Vacancy.class));
    }

    @Test
    @DisplayName("Should delete an existing vacancy")
    void deleteVacancy() {
        when(vacancyRepository.existsById(101L)).thenReturn(true);
        doNothing().when(vacancyRepository).deleteById(101L);

        vacancyService.deleteVacancy(101L);

        verify(vacancyRepository, times(1)).existsById(101L);
        verify(vacancyRepository, times(1)).deleteById(101L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting a non-existent vacancy")
    void deleteVacancy_NotFound() {
        when(vacancyRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> vacancyService.deleteVacancy(999L));
        verify(vacancyRepository, times(1)).existsById(999L);
        verify(vacancyRepository, never()).deleteById(anyLong());
    }
}