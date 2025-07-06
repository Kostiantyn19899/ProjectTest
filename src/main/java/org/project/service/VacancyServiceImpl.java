package org.project.service;

import lombok.RequiredArgsConstructor;
import org.project.dto.VacancyRequestDto;
import org.project.entity.Project;
import org.project.entity.Vacancy;
import org.project.exception.ProjectNotFoundException;
import org.project.repository.ProjectJpaRepository;
import org.project.repository.VacancyJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyJpaRepository vacancyRepository;

    private final ProjectJpaRepository projectRepository;

    @Override
    public List<Vacancy> getByProjectId(Long projectId) {
        if (vacancyRepository.findByProjectId(projectId).isEmpty()) {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
        return vacancyRepository.findByProjectId(projectId);
    }

    @Override
    public Vacancy addVacancyToProject(VacancyRequestDto requestDto, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        Vacancy vacancy = Vacancy.builder()
                .name(requestDto.name())
                .field(requestDto.field())
                .experience(requestDto.experience())
                .country(requestDto.country())
                .description(requestDto.description())
                .project(project) // Link vacancy to project
                .build();
        return vacancyRepository.save(vacancy);

    }
}
//
//    @Override
//    public Project getById(Long id) {
//        return repository.findById(id)
//                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
//    }
//
//
//    }
//
//    @Override
//    @Transactional
//    public void delete(Long id) {
//        Project project = getById(id);
//        repository.deleteById(project.getId());
//    }
//
//    @Override
//    public Project update(Long id, ProjectRequestDto requestDto) {
//        Project project = getById(id);
//        project.setName(requestDto.name());
//        project.setField(requestDto.field());
//        project.setExperience(requestDto.experience());
//        project.setDescription(requestDto.description());
//        project.setDeadline(requestDto.deadline());
//        return create(project);
//    }




