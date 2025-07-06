package org.project.service;

import org.project.dto.ProjectRequestDto;
import org.project.dto.VacancyRequestDto;
import org.project.entity.Project;
import org.project.entity.Vacancy;

import java.util.List;


public interface VacancyService {

    List<Vacancy> getByProjectId(Long projectId);

    Vacancy addVacancyToProject(VacancyRequestDto requestDto, Long projectId);

    Vacancy updateVacancy(Long id, VacancyRequestDto requestDto);

    void deleteVacancy(Long id);

}
