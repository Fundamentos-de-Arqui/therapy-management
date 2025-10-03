package com.soulware.therapymanagement.infrastructure.persistence.jpa.repositories;

import com.soulware.therapymanagement.domain.model.aggregates.WeeklySessions;
import com.soulware.therapymanagement.domain.model.valueobjects.YearWeek;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.LegalResponsibleId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.WeeklySessionsId;
import com.soulware.therapymanagement.domain.repositories.WeeklySessionsRepository;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.WeeklySessionsEntity;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers.WeeklySessionsMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaWeeklySessionsRepository implements WeeklySessionsRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final WeeklySessionsMapper weeklySessionsMapper;

    @Inject
    public JpaWeeklySessionsRepository(WeeklySessionsMapper weeklySessionsMapper) {
        this.weeklySessionsMapper = weeklySessionsMapper;
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<WeeklySessions> findById(WeeklySessionsId id) {
        if (id == null) {
            return Optional.empty();
        }

        WeeklySessionsEntity entity = entityManager.find(WeeklySessionsEntity.class, id.value());

        return Optional.ofNullable(entity)
                .map(weeklySessionsMapper::toDomain);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void save(WeeklySessions weeklySessions) {
        if (weeklySessions == null) {
            return;
        }

        WeeklySessionsEntity entity = weeklySessionsMapper.toEntity(weeklySessions);

        entityManager.merge(entity);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<WeeklySessions> findByTherapyPlanId(TherapyPlanId therapyPlanId) {
        if (therapyPlanId == null) {
            return List.of();
        }

        List<WeeklySessionsEntity> entities = entityManager.createQuery(
                        "SELECT ws FROM WeeklySessionsEntity ws WHERE ws.therapyPlanId = :planId",
                        WeeklySessionsEntity.class)
                .setParameter("planId", therapyPlanId.value())
                .getResultList();

        return entities.stream()
                .map(weeklySessionsMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WeeklySessions> findByPlanAndWeek(TherapyPlanId therapyPlanId, YearWeek yearWeek) {
        return Optional.empty();
    }

    @Override
    public List<WeeklySessions> findByLegalResponsibleId(LegalResponsibleId legalResponsibleId) {
        return List.of();
    }

    @Override
    public List<WeeklySessions> findByPatientId(PatientId patientId) {
        return List.of();
    }
}
