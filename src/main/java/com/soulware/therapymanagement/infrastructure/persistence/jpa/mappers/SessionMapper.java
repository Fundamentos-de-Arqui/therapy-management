package com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers;

import com.soulware.therapymanagement.domain.model.entities.Session;
import com.soulware.therapymanagement.domain.model.valueobjects.SessionStatus;
import com.soulware.therapymanagement.domain.model.valueobjects.TimeSlot;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.SessionId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.SessionEntity;
import jakarta.inject.Inject;

public record SessionMapper(SessionStatusMapper statusMapper) {
    @Inject
    public SessionMapper {
    }

    /**
     * Converts a Session domain model to a SessionEntity persistence model.
     * Audit fields are NOT set here, as they are handled by JPA lifecycle events.
     */
    public SessionEntity toEntity(Session domain) {
        if (domain == null) {
            return null;
        }

        SessionEntity entity = new SessionEntity();

        entity.setId(domain.getId().value());
        entity.setStartTime(domain.getSlot().start());
        entity.setEndTime(domain.getSlot().end());
        entity.setTherapistId(domain.getTherapistId().value());
        entity.setStatus(statusMapper.toEntity(domain.getStatus()));

        return entity;
    }

    /**
     * Converts a SessionEntity persistence model to a Session domain model.
     * Audit fields are excluded from the domain model.
     */
    public Session toDomain(SessionEntity entity) {
        if (entity == null) {
            return null;
        }

        SessionId id = new SessionId(entity.getId());
        TimeSlot slot = new TimeSlot(entity.getStartTime(), entity.getEndTime());
        TherapistId therapistId = new TherapistId(entity.getTherapistId());
        SessionStatus status = statusMapper.toDomain(entity.getStatus());

        return new Session(
                id,
                slot,
                therapistId,
                status
        );
    }
}
