package com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers;

import com.soulware.therapymanagement.domain.model.aggregates.WeeklySessions;
import com.soulware.therapymanagement.domain.model.valueobjects.PlanWeek;
import com.soulware.therapymanagement.domain.model.valueobjects.YearWeek;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.LegalResponsibleId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.WeeklySessionsId;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.WeeklySessionsEntity;
import com.soulware.therapymanagement.shared.domain.model.events.DomainEventPublisher;
import jakarta.inject.Inject;

import java.util.stream.Collectors;

public record WeeklySessionsMapper(SessionMapper sessionMapper, DomainEventPublisher eventPublisher) {
    @Inject
    public WeeklySessionsMapper {
    }

    /**
     * Converts a WeeklySessions domain model to a WeeklySessionsEntity persistence model.
     *
     * @param domain The domain model.
     * @return The persistence entity.
     */
    public WeeklySessionsEntity toEntity(WeeklySessions domain) {
        if (domain == null) {
            return null;
        }

        WeeklySessionsEntity entity = new WeeklySessionsEntity();

        entity.setId(domain.getId().value());
        entity.setPlanWeekNumber(domain.getPlanWeek().value());
        entity.setYearWeek(domain.getYearWeek().toString());
        entity.setTherapyPlanId(domain.getTherapyPlanId().value());
        entity.setLegalResponsibleId(domain.getLegalResponsibleId().value());

        if (domain.getSessions() != null) {
            entity.setSessions(domain.getSessions().stream()
                    .map(sessionMapper::toEntity)
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    /**
     * Converts a WeeklySessionsEntity persistence model to a WeeklySessions domain model.
     * Audit fields are explicitly excluded from the domain object creation.
     */
    public WeeklySessions toDomain(WeeklySessionsEntity entity) {
        if (entity == null) {
            return null;
        }

        WeeklySessionsId id = new WeeklySessionsId(entity.getId());
        PlanWeek planWeek = new PlanWeek(entity.getPlanWeekNumber());
        YearWeek yearWeek = YearWeek.fromFormattedString(entity.getYearWeek());
        TherapyPlanId therapyPlanId = new TherapyPlanId(entity.getTherapyPlanId());
        LegalResponsibleId legalResponsibleId = new LegalResponsibleId(entity.getLegalResponsibleId());

        var sessions = entity.getSessions().stream()
                .map(sessionMapper::toDomain)
                .collect(Collectors.toList());

        return new WeeklySessions(
                id,
                this.eventPublisher,
                planWeek,
                yearWeek,
                therapyPlanId,
                sessions,
                legalResponsibleId
        );
    }
}
