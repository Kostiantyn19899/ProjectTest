package org.project.dto;

import org.mapstruct.Mapper;
import org.project.entity.Project;

@Mapper(componentModel = "spring")
public abstract class ProjectMapper {

    public abstract Project toEntity(ProjectRequestDto projectRequestDto);

    public abstract ProjectResponseDto toDto(Project project);
}

