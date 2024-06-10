package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Alerte;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Alerte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Long> {}
