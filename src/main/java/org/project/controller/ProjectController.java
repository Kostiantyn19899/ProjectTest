package org.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.dto.ProjectMapper;
import org.project.dto.ProjectRequestDto;
import org.project.dto.ProjectResponseDto;
import org.project.entity.Project;
import org.project.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {

    private final ProjectMapper projectMapper;

    private final ProjectService projectService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProjectResponseDto>> getAll() {
        List<ProjectResponseDto> dtolist = projectService.getAll().stream()
                .map(projectMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtolist, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ProjectResponseDto> getById(@PathVariable Long id) {
        Project category = projectService.getById(id);
        return new ResponseEntity<>(projectMapper.toDto(category), HttpStatus.OK);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProjectResponseDto> create(@Valid @RequestBody ProjectRequestDto requestDto) {
        Project entity = projectMapper.toEntity(requestDto);
        Project createdCategory = projectService.create(entity);
        return new ResponseEntity<>(projectMapper.toDto(createdCategory), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ProjectResponseDto> update(@Valid @PathVariable(name = "id") Long id, @RequestBody ProjectRequestDto requestDto) {
        Project category = projectService.update(id, requestDto);
        return new ResponseEntity<>(projectMapper.toDto(category), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
