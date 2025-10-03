package com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers;

import com.soulware.therapymanagement.domain.model.valueobjects.SessionStatus;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.SessionStatusEntity;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.repositories.SessionStatusRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

public record SessionStatusMapper(SessionStatusRepository statusRepository) {
    @Inject
    public SessionStatusMapper {
    }

    /**
     * Converts a SessionStatusEntity persistence model to a SessionStatus domain model.
     */
    public SessionStatus toDomain(SessionStatusEntity entity) {
        if (entity == null) {
            return null;
        }

        return SessionStatus.valueOf(entity.getName());
    }

    /**
     * Converts a SessionStatus domain model to a SessionStatusEntity persistence model.
     * Note: This method is primarily used for setting the FK on the SessionEntity.
     */
    public SessionStatusEntity toEntity(SessionStatus domain) {
        if (domain == null) {
            return null;
        }

        String statusName = domain.name();

        return statusRepository.findByName(statusName)
                .orElseThrow(() -> new EntityNotFoundException("Session status not found for name: " + statusName));
    }
}
