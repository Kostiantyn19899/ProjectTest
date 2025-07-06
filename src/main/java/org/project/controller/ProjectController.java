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
    public ResponseEntity<List<ProjectResponseDto>> getAll() {
        List<ProjectResponseDto> dtoList = projectService.getAll().stream()
                .map(projectMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getById(@PathVariable Long id) {
        Project project = projectService.getById(id);
        return new ResponseEntity<>(projectMapper.toDto(project), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto> create(@Valid @RequestBody ProjectRequestDto requestDto) {
        Project entity = projectMapper.toEntity(requestDto);
        Project createdProject = projectService.create(entity);
        return new ResponseEntity<>(projectMapper.toDto(createdProject), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> update(@Valid @PathVariable(name = "id") Long id, @RequestBody ProjectRequestDto requestDto) {
        Project project = projectService.update(id, requestDto);
        return new ResponseEntity<>(projectMapper.toDto(project), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
