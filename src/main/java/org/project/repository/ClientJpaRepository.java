package org.project.repository;

import org.project.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientJpaRepository extends JpaRepository<Client, Long> {
}
