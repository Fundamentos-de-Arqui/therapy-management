package com.soulware.therapymanagement.infrastructure.persistence.jpa.repositories;

import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.SessionStatusEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class JpaSessionStatusRepository implements SessionStatusRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<SessionStatusEntity> findByName(String name) {
        if (name == null || name.isEmpty()) {
            return Optional.empty();
        }

        try {
            SessionStatusEntity entity = entityManager.createQuery(
                            "SELECT s FROM SessionStatusEntity s WHERE s.name = :statusName", SessionStatusEntity.class)
                    .setParameter("statusName", name)
                    .getSingleResult();

            return Optional.of(entity);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
