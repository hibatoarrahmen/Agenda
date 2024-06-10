package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Employe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface EmployeRepositoryWithBagRelationships {
    Optional<Employe> fetchBagRelationships(Optional<Employe> employe);

    List<Employe> fetchBagRelationships(List<Employe> employes);

    Page<Employe> fetchBagRelationships(Page<Employe> employes);
}
