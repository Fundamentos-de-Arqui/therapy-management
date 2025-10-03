package com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers;

import com.soulware.therapymanagement.domain.model.aggregates.TherapyPlan;
import com.soulware.therapymanagement.domain.model.valueobjects.TherapyPlanStatus;
import com.soulware.therapymanagement.domain.model.valueobjects.WeeklySchedule;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.LegalResponsibleId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.TherapyPlanEntity;
import com.soulware.therapymanagement.shared.domain.model.events.DomainEventPublisher;
import jakarta.inject.Inject;

public record TherapyPlanMapper(DomainEventPublisher eventPublisher, TherapyPlanStatusMapper statusMapper,
                                WeeklyScheduleMapper scheduleMapper) {
    @Inject
    public TherapyPlanMapper {
    }

    /**
     * Converts a TherapyPlan domain model to a TherapyPlanEntity persistence model.
     *
     * @param domain The domain model.
     * @return The persistence entity.
     */
    public TherapyPlanEntity toEntity(TherapyPlan domain) {
        if (domain == null) {
            return null;
        }

        TherapyPlanEntity entity = new TherapyPlanEntity();

        entity.setId(domain.getId().value());
        entity.setAssignedTherapistId(domain.getAssignedTherapistId().value());
        entity.setPatientId(domain.getPatientId().value());
        entity.setLegalResponsibleId(domain.getLegalResponsibleId().value());

        entity.setStatus(statusMapper.toEntity(domain.getStatus()));
        entity.setScheduleLayout(scheduleMapper.toEntity(domain.getSchedule(), domain.getId().value()));

        return entity;
    }

    /**
     * Converts a TherapyPlanEntity persistence model to a TherapyPlan domain model.
     * Audit fields are explicitly excluded from the domain object creation.
     */
    public TherapyPlan toDomain(TherapyPlanEntity entity) {
        if (entity == null) {
            return null;
        }

        TherapyPlanId id = new TherapyPlanId(entity.getId());
        TherapistId assignedTherapistId = new TherapistId(entity.getAssignedTherapistId());
        PatientId patientId = new PatientId(entity.getPatientId());
        LegalResponsibleId legalResponsibleId = new LegalResponsibleId(entity.getLegalResponsibleId());

        TherapyPlanStatus status = statusMapper.toDomain(entity.getStatus());
        WeeklySchedule schedule = scheduleMapper.toDomain(entity.getWeeklySchedule());

        return new TherapyPlan(
                id,
                this.eventPublisher,
                assignedTherapistId,
                patientId,
                legalResponsibleId,
                status,
                schedule
        );
    }
}
