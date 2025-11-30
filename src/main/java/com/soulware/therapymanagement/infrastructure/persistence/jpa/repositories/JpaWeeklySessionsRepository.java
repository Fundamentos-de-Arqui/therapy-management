package com.soulware.therapymanagement.infrastructure.persistence.jpa.repositories;

import com.soulware.therapymanagement.domain.model.aggregates.WeeklySessions;
import com.soulware.therapymanagement.domain.model.valueobjects.YearWeek;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.AssessmentId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.WeeklySessionsId;
import com.soulware.therapymanagement.domain.repositories.WeeklySessionsRepository;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.TherapyPlanEntity;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.TherapyScheduleEntryEntity;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.WeeklySessionsEntity;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers.WeeklySessionsMapper;
import com.soulware.therapymanagement.shared.domain.model.entities.SessionQueryResult;
import com.soulware.therapymanagement.shared.infrastructure.PagedResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
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
    @Transactional(Transactional.TxType.SUPPORTS)
    public PagedResult<SessionQueryResult> findByFilters(
            Long therapistId,
            Long legalResponsibleId,
            Long patientId,
            String status,
            int page,
            int size
    ) {

        // --- MAIN QUERY ---
        StringBuilder sql = new StringBuilder("""
        SELECT tsee.entryId,
               tpe.assigned_therapist_id,
               tpe.patient_id,
               tpe.legal_responsible_id,
               tsee.start_time,
               tsee.end_time,
               s.name
        FROM therapy_schedule_entries tsee
        JOIN therapy_plans tpe ON tsee.therapy_plan_id = tpe.id
        JOIN session_statuses s ON tsee.status_id = s.id
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        if (therapistId != null) {
            sql.append(" AND tpe.assigned_therapist_id = ?");
            params.add(therapistId);
        }

        if (legalResponsibleId != null) {
            sql.append(" AND tpe.legal_responsible_id = ?");
            params.add(legalResponsibleId);
        }

        if (patientId != null) {
            sql.append(" AND tpe.patient_id = ?");
            params.add(patientId);
        }

        if (status != null) {
            sql.append(" AND s.name = ?");
            params.add(status);
        }

        sql.append(" ORDER BY tsee.start_time LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);

        Query query = entityManager.createNativeQuery(sql.toString());

        // Set positional parameters
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }

        List<Object[]> rows = query.getResultList();

        List<SessionQueryResult> items = rows.stream()
                .map(r -> new SessionQueryResult(
                        ((Number) r[0]).longValue(),
                        ((Number) r[1]).longValue(),
                        ((Number) r[2]).longValue(),
                        ((Number) r[3]).longValue(),
                        ((java.sql.Timestamp) r[4]).toInstant().atZone(ZoneId.systemDefault()),
                        ((java.sql.Timestamp) r[5]).toInstant().atZone(ZoneId.systemDefault()),
                        (String) r[6]
                ))
                .toList();

        // --- COUNT QUERY ---
        StringBuilder countSql = new StringBuilder("""
        SELECT COUNT(tsee.entryId)
        FROM therapy_schedule_entries tsee
        JOIN therapy_plans tpe ON tsee.therapy_plan_id = tpe.id
        JOIN session_statuses s ON tsee.status_id = s.id
        WHERE 1=1
    """);

        List<Object> countParams = new ArrayList<>();

        if (therapistId != null) {
            countSql.append(" AND tpe.assigned_therapist_id = ?");
            countParams.add(therapistId);
        }

        if (legalResponsibleId != null) {
            countSql.append(" AND tpe.legal_responsible_id = ?");
            countParams.add(legalResponsibleId);
        }

        if (patientId != null) {
            countSql.append(" AND tpe.patient_id = ?");
            countParams.add(patientId);
        }

        if (status != null) {
            countSql.append(" AND s.name = ?");
            countParams.add(status);
        }

        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        for (int i = 0; i < countParams.size(); i++) {
            countQuery.setParameter(i + 1, countParams.get(i));
        }

        Long totalItems = ((Number) countQuery.getSingleResult()).longValue();

        return new PagedResult<>(items, totalItems, page, size);
    }

    @Override
    public Optional<WeeklySessions> findByPlanAndWeek(TherapyPlanId therapyPlanId, YearWeek yearWeek) {
        return Optional.empty();
    }

    @Override
    public List<WeeklySessions> findByLegalResponsibleId(AssessmentId legalResponsibleId) {
        return List.of();
    }

    @Override
    public List<WeeklySessions> findByPatientId(PatientId patientId) {
        return List.of();
    }
}
