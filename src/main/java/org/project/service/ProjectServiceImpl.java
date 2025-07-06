package org.project.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.project.dto.ProjectRequestDto;
import org.project.entity.Project;
import org.project.exception.ProjectAlreadyExistsException;
import org.project.exception.ProjectNotFoundException;
import org.project.repository.ProjectJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectJpaRepository repository;

    @Override
    public List<Project> getAll() {
        return repository.findAll();
    }

    @Override
    public Project getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
    }

    @Override
    @Transactional
    public Project create(Project project) {
        if (repository.findByName(project.getName()).isPresent()) {
            throw new ProjectAlreadyExistsException("Project already exists with name: " + project.getName());
        }
        return repository.save(project);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Project project = getById(id);
        repository.deleteById(project.getId());
    }

    @Override
    @Transactional
    public Project update(Long id, ProjectRequestDto requestDto) {
        Project project = getById(id);

        project.setName(requestDto.name());
        project.setField(requestDto.field());
        project.setExperience(requestDto.experience());
        project.setDescription(requestDto.description());
        project.setDeadline(requestDto.deadline());

        return repository.save(project);
    }
}


