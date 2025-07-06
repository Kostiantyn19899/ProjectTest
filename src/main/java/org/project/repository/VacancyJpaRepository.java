package org.project.repository;

import org.project.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyJpaRepository extends JpaRepository<Vacancy, Long> {
}
