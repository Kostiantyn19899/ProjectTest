package org.project.dto;

import java.time.LocalDate;

public record ProjectResponseDto (Long id,
                                  String name,
                                  String field,
                                  String experience,
                                  String description,
                                  LocalDate deadline){
}
