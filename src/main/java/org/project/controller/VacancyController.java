package org.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.dto.*;
import org.project.entity.Vacancy;
import org.project.service.VacancyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class VacancyController implements VacancyApi {

    private final VacancyMapper vacancyMapper;

    private final VacancyService vacancyService;

    @GetMapping("/{id}/vacancies")
    public ResponseEntity<List<VacancyResponseDto>> getVacanciesByProjectId(@PathVariable Long id) {
        List<VacancyResponseDto> dtoList = vacancyService.getByProjectId(id).stream()
                .map(vacancyMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/{id}/vacancies")
    public ResponseEntity<VacancyResponseDto> create(@Valid @PathVariable(name = "id") Long id, @RequestBody VacancyRequestDto requestDto) {
        Vacancy vacancy = vacancyService.addVacancyToProject(requestDto,id);
        return new ResponseEntity<>(vacancyMapper.toDto(vacancy), HttpStatus.CREATED);
    }

    @PutMapping("/vacancies/{id}")
    public ResponseEntity<VacancyResponseDto> updateVacancy(@PathVariable Long id, @Valid @RequestBody VacancyRequestDto request) {
        Vacancy vacancy = vacancyService.updateVacancy(id, request);
        return new ResponseEntity<>(vacancyMapper.toDto(vacancy), HttpStatus.OK);
    }

    @DeleteMapping("/vacancies/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.noContent().build();
    }
}
