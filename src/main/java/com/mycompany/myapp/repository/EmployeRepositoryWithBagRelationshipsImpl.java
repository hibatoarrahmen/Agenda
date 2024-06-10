package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Employe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EmployeRepositoryWithBagRelationshipsImpl implements EmployeRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String EMPLOYES_PARAMETER = "employes";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Employe> fetchBagRelationships(Optional<Employe> employe) {
        return employe.map(this::fetchActiviteDepts);
    }

    @Override
    public Page<Employe> fetchBagRelationships(Page<Employe> employes) {
        return new PageImpl<>(fetchBagRelationships(employes.getContent()), employes.getPageable(), employes.getTotalElements());
    }

    @Override
    public List<Employe> fetchBagRelationships(List<Employe> employes) {
        return Optional.of(employes).map(this::fetchActiviteDepts).orElse(Collections.emptyList());
    }

    Employe fetchActiviteDepts(Employe result) {
        return entityManager
            .createQuery("select employe from Employe employe left join fetch employe.activiteDepts where employe.id = :id", Employe.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Employe> fetchActiviteDepts(List<Employe> employes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, employes.size()).forEach(index -> order.put(employes.get(index).getId(), index));
        List<Employe> result = entityManager
            .createQuery(
                "select employe from Employe employe left join fetch employe.activiteDepts where employe in :employes",
                Employe.class
            )
            .setParameter(EMPLOYES_PARAMETER, employes)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
