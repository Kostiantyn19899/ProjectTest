package org.project.service;

import org.project.dto.ProjectRequestDto;
import org.project.entity.Project;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProjectService {

    List<Project> getAll();

    Project getById(Long id);

    Project create(Project category);

    Project update(Long id, ProjectRequestDto requestDto);

    void delete(Long id);



}
