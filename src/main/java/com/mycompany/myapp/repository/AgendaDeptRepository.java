package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AgendaDept;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AgendaDept entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgendaDeptRepository extends JpaRepository<AgendaDept, Long> {}
