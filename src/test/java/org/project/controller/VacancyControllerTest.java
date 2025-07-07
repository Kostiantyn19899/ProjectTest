package org.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.dto.VacancyMapper;
import org.project.dto.VacancyRequestDto;
import org.project.dto.VacancyResponseDto;
import org.project.entity.Project;
import org.project.entity.Vacancy;
import org.project.exception.ProjectNotFoundException;
import org.project.exception.VacancyNotFoundException;
import org.project.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VacancyController.class)
class VacancyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VacancyService vacancyService;

    @MockBean
    private VacancyMapper vacancyMapper;

    private Vacancy vacancy1;
    private Vacancy vacancy2;
    private VacancyRequestDto vacancyRequestDto;
    private VacancyResponseDto vacancyResponseDto1;
    private VacancyResponseDto vacancyResponseDto2;
    private Project project;

    @BeforeEach
    void setUp() {
        project = Project.builder().id(1L).name("Project Alpha").build();

        vacancy1 = Vacancy.builder()
                .id(101L)
                .name("Java Dev")
                .field("Backend")
                .experience("Mid")
                .country("Germany")
                .description("Java developer for Spring Boot.")
                .project(project)
                .build();

        vacancy2 = Vacancy.builder()
                .id(102L)
                .name("React Dev")
                .field("Frontend")
                .experience("Junior")
                .country("France")
                .description("Frontend developer for React JS.")
                .project(project)
                .build();

        vacancyRequestDto = new VacancyRequestDto(
                "New Vacancy Name",
                "New Field",
                "New Experience",
                "New Country",
                "New Description"
        );

        vacancyResponseDto1 = new VacancyResponseDto(
                101L,
                "Java Dev",
                "Backend",
                "Mid",
                "Germany",
                "Java developer for Spring Boot.",
                1L
        );

        vacancyResponseDto2 = new VacancyResponseDto(
                102L,
                "React Dev",
                "Frontend",
                "Junior",
                "France",
                "Frontend developer for React JS.",
                1L
        );
    }

    @Test
    @DisplayName("GET /projects/{id}/vacancies should return vacancies for a project")
    void getVacanciesByProjectId() throws Exception {
        List<Vacancy> vacancies = Arrays.asList(vacancy1, vacancy2);
        List<VacancyResponseDto> dtos = Arrays.asList(vacancyResponseDto1, vacancyResponseDto2);

        when(vacancyService.getByProjectId(1L)).thenReturn(vacancies);
        when(vacancyMapper.toDto(vacancy1)).thenReturn(vacancyResponseDto1);
        when(vacancyMapper.toDto(vacancy2)).thenReturn(vacancyResponseDto2);

        mockMvc.perform(get("/projects/{id}/vacancies", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(vacancyResponseDto1.id()))
                .andExpect(jsonPath("$[1].name").value(vacancyResponseDto2.name()));

        verify(vacancyService, times(1)).getByProjectId(1L);
        verify(vacancyMapper, times(2)).toDto(any(Vacancy.class));
    }

    @Test
    @DisplayName("GET /projects/{id}/vacancies should return 404 if project not found")
    void getVacanciesByProjectId_ProjectNotFound() throws Exception {
        when(vacancyService.getByProjectId(anyLong())).thenThrow(new ProjectNotFoundException("Project not found"));

        mockMvc.perform(get("/projects/{id}/vacancies", 99L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Project not found"));

        verify(vacancyService, times(1)).getByProjectId(99L);
        verify(vacancyMapper, never()).toDto(any(Vacancy.class));
    }

    @Test
    @DisplayName("POST /projects/{id}/vacancies should create a new vacancy for a project")
    void createVacancyForProject() throws Exception {
        Vacancy createdVacancy = Vacancy.builder()
                .id(103L)
                .name(vacancyRequestDto.name())
                .field(vacancyRequestDto.field())
                .experience(vacancyRequestDto.experience())
                .country(vacancyRequestDto.country())
                .description(vacancyRequestDto.description())
                .project(project)
                .build();
        VacancyResponseDto createdVacancyDto = new VacancyResponseDto(
                103L,
                vacancyRequestDto.name(),
                vacancyRequestDto.field(),
                vacancyRequestDto.experience(),
                vacancyRequestDto.country(),
                vacancyRequestDto.description(),
                1L
        );

        when(vacancyService.addVacancyToProject(any(VacancyRequestDto.class), eq(1L))).thenReturn(createdVacancy);
        when(vacancyMapper.toDto(any(Vacancy.class))).thenReturn(createdVacancyDto);

        mockMvc.perform(post("/projects/{id}/vacancies", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vacancyRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdVacancyDto.id()))
                .andExpect(jsonPath("$.name").value(createdVacancyDto.name()));

        verify(vacancyService, times(1)).addVacancyToProject(vacancyRequestDto, 1L);
        verify(vacancyMapper, times(1)).toDto(createdVacancy);
    }

    @Test
    @DisplayName("POST /projects/{id}/vacancies should return 404 if project not found when creating vacancy")
    void createVacancyForProject_ProjectNotFound() throws Exception {
        when(vacancyService.addVacancyToProject(any(VacancyRequestDto.class), anyLong()))
                .thenThrow(new ProjectNotFoundException("Project not found"));

        mockMvc.perform(post("/projects/{id}/vacancies", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vacancyRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Project not found"));

        verify(vacancyService, times(1)).addVacancyToProject(vacancyRequestDto, 99L);
        verify(vacancyMapper, never()).toDto(any(Vacancy.class));
    }

    @Test
    @DisplayName("PUT /projects/vacancies/{id} should update an existing vacancy")
    void updateVacancy() throws Exception {
        Vacancy updatedVacancy = Vacancy.builder()
                .id(101L)
                .name(vacancyRequestDto.name())
                .field(vacancyRequestDto.field())
                .experience(vacancyRequestDto.experience())
                .country(vacancyRequestDto.country())
                .description(vacancyRequestDto.description())
                .project(project)
                .build();
        VacancyResponseDto updatedVacancyDto = new VacancyResponseDto(
                101L,
                vacancyRequestDto.name(),
                vacancyRequestDto.field(),
                vacancyRequestDto.experience(),
                vacancyRequestDto.country(),
                vacancyRequestDto.description(),
                1L
        );

        when(vacancyService.updateVacancy(eq(101L), any(VacancyRequestDto.class))).thenReturn(updatedVacancy);
        when(vacancyMapper.toDto(any(Vacancy.class))).thenReturn(updatedVacancyDto);

        mockMvc.perform(put("/projects/vacancies/{id}", 101L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vacancyRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedVacancyDto.id()))
                .andExpect(jsonPath("$.name").value(updatedVacancyDto.name()));

        verify(vacancyService, times(1)).updateVacancy(101L, vacancyRequestDto);
        verify(vacancyMapper, times(1)).toDto(updatedVacancy);
    }

    @Test
    @DisplayName("PUT /projects/vacancies/{id} should return 404 for not found vacancy during update")
    void updateVacancy_NotFound() throws Exception {
        when(vacancyService.updateVacancy(anyLong(), any(VacancyRequestDto.class)))
                .thenThrow(new VacancyNotFoundException("Vacancy not found"));

        mockMvc.perform(put("/projects/vacancies/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vacancyRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vacancy not found"));

        verify(vacancyService, times(1)).updateVacancy(999L, vacancyRequestDto);
        verify(vacancyMapper, never()).toDto(any(Vacancy.class));
    }

    @Test
    @DisplayName("DELETE /projects/vacancies/{id} should delete a vacancy")
    void deleteVacancy() throws Exception {
        doNothing().when(vacancyService).deleteVacancy(101L);

        mockMvc.perform(delete("/projects/vacancies/{id}", 101L))
                .andExpect(status().isNoContent()); // ResponseEntity.noContent().build() returns 204

        verify(vacancyService, times(1)).deleteVacancy(101L);
    }

    @Test
    @DisplayName("DELETE /projects/vacancies/{id} should return 404 for not found vacancy during delete")
    void deleteVacancy_NotFound() throws Exception {
        doThrow(new VacancyNotFoundException("Vacancy not found")).when(vacancyService).deleteVacancy(anyLong());

        mockMvc.perform(delete("/projects/vacancies/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vacancy not found"));

        verify(vacancyService, times(1)).deleteVacancy(999L);
    }
}