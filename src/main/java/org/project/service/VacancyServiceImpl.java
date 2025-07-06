package org.project.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.project.dto.VacancyRequestDto;
import org.project.entity.Project;
import org.project.entity.Vacancy;
import org.project.exception.ProjectNotFoundException;
import org.project.exception.VacancyNotFoundException;
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
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
        return vacancyRepository.findByProjectId(projectId);
    }

    @Override
    @Transactional
    public Vacancy addVacancyToProject(VacancyRequestDto requestDto, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        Vacancy vacancy = Vacancy.builder()
                .name(requestDto.name())
                .field(requestDto.field())
                .experience(requestDto.experience())
                .country(requestDto.country())
                .description(requestDto.description())
                .project(project)
                .build();
        return vacancyRepository.save(vacancy);
    }

    @Transactional
    @Override
    public Vacancy updateVacancy(Long id, VacancyRequestDto requestDto) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new VacancyNotFoundException("Vacancy not found with id: " + id));

        vacancy.setName(requestDto.name());
        vacancy.setField(requestDto.field());
        vacancy.setExperience(requestDto.experience());
        vacancy.setCountry(requestDto.country());
        vacancy.setDescription(requestDto.description());
        return vacancyRepository.save(vacancy);
    }

    @Override
    @Transactional
    public void deleteVacancy(Long id) {
        if (!vacancyRepository.existsById(id)) {
            throw new VacancyNotFoundException("Vacancy not found with id: " + id);
        }
        vacancyRepository.deleteById(id);
    }
}


