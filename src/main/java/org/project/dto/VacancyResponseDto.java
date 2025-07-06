package org.project.dto;

import java.time.LocalDate;

public record VacancyResponseDto( Long id,
                                  String name,
                                  String field,
                                  String experience,
                                  String country,
                                  String description,
                                  Long projectId){
}
