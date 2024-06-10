package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProcesVerbal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProcesVerbal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcesVerbalRepository extends JpaRepository<ProcesVerbal, Long> {}
