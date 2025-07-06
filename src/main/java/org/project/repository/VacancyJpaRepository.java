package org.project.repository;

import org.project.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VacancyJpaRepository extends JpaRepository<Vacancy, Long> {

    List<Vacancy> findByProjectId(Long projectId);
}
