package com.soulware.therapymanagement.infrastructure.persistence.jpa.repositories;

import com.soulware.therapymanagement.domain.model.aggregates.TherapyPlan;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.domain.repositories.TherapyPlanRepository;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.TherapyPlanEntity;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers.TherapyPlanMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaTherapyPlanRepository implements TherapyPlanRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final TherapyPlanMapper therapyPlanMapper;

    @Inject
    public JpaTherapyPlanRepository(TherapyPlanMapper therapyPlanMapper) {
        this.therapyPlanMapper = therapyPlanMapper;
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<TherapyPlan> findById(TherapyPlanId id) {
        if (id == null) {
            return Optional.empty();
        }
        TherapyPlanEntity entity = entityManager.find(TherapyPlanEntity.class, id.value());
        return Optional.ofNullable(entity)
                .map(therapyPlanMapper::toDomain);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void save(TherapyPlan plan) {
        if (plan == null) {
            return;
        }
        TherapyPlanEntity entity = therapyPlanMapper.toEntity(plan);
        entityManager.merge(entity);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<TherapyPlan> findByAssignedTherapistId(TherapistId therapistId) {
        if (therapistId == null) {
            return List.of();
        }

        List<TherapyPlanEntity> entities = entityManager.createQuery(
                        "SELECT tp FROM TherapyPlanEntity tp WHERE tp.assignedTherapistId = :therapistId",
                        TherapyPlanEntity.class)
                .setParameter("therapistId", therapistId.value())
                .getResultList();

        return entities.stream()
                .map(therapyPlanMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<TherapyPlan> findActivePlanByPatientId(PatientId patientId) {
        if (patientId == null) {
            return Optional.empty();
        }

        try {
            TherapyPlanEntity entity = entityManager.createQuery(
                            "SELECT tp FROM TherapyPlanEntity tp JOIN tp.status status " +
                                    "WHERE tp.patientId = :patientId AND status.name = 'ACTIVE'",
                            TherapyPlanEntity.class)
                    .setParameter("patientId", patientId.value())
                    .getSingleResult();

            return Optional.of(therapyPlanMapper.toDomain(entity));

        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }
}
