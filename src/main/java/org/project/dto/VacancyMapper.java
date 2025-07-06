package org.project.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.project.entity.Vacancy;

@Mapper(componentModel = "spring")
public abstract class VacancyMapper {

    public abstract Vacancy toEntity(VacancyRequestDto vacancyRequestDto);

    @Mapping(source = "project.id", target = "projectId")
    public abstract VacancyResponseDto toDto(Vacancy vacancy);
}

