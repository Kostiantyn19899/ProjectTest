package org.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.dto.*;
import org.project.entity.Project;
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
public class VacancyController {

    private final VacancyMapper vacancyMapper;

    private final VacancyService vacancyService;


    @GetMapping("/{id}/vacancies")
    public ResponseEntity<List<VacancyResponseDto>> getVacanciesByProjectId(@PathVariable Long id) {
        List<VacancyResponseDto> dtoList = vacancyService.getByProjectId(id).stream()
                .map(vacancyMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/{id}/vacancies")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VacancyResponseDto> create(@Valid @PathVariable(name = "id") Long id, @RequestBody VacancyRequestDto requestDto) {
        Vacancy vacancy = vacancyService.addVacancyToProject(requestDto,id);
        return new ResponseEntity<>(vacancyMapper.toDto(vacancy), HttpStatus.CREATED);
    }
}
