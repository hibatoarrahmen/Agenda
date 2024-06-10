package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ActiviteDept;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ActiviteDept entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActiviteDeptRepository extends JpaRepository<ActiviteDept, Long> {}
