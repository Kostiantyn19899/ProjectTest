package org.project.dto;

import org.mapstruct.Mapper;
import org.project.entity.Project;
import org.project.entity.Vacancy;

@Mapper(componentModel = "spring")
public abstract class VacancyMapper {

    public abstract Vacancy toEntity(VacancyRequestDto vacancyRequestDto);

    public abstract VacancyResponseDto toDto(Vacancy vacancy);
}

