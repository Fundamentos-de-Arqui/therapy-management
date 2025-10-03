package com.soulware.therapymanagement.infrastructure.persistence.jpa.repositories;

import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.TherapyPlanStatusEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class JpaTherapyPlanStatusRepository implements TherapyPlanStatusRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Finds a TherapyPlanStatusEntity by its name field for Foreign Key mapping.
     * @param name The name of the status (e.g., "ACTIVE").
     * @return An Optional containing the matched entity.
     */
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<TherapyPlanStatusEntity> findByName(String name) {
        if (name == null || name.isEmpty()) {
            return Optional.empty();
        }

        try {
            TherapyPlanStatusEntity entity = entityManager.createQuery(
                            "SELECT s FROM TherapyPlanStatusEntity s WHERE s.name = :statusName", TherapyPlanStatusEntity.class)
                    .setParameter("statusName", name)
                    .getSingleResult();

            return Optional.of(entity);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
