package com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers;

import com.soulware.therapymanagement.domain.model.valueobjects.TherapyPlanStatus;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.TherapyPlanStatusEntity;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.repositories.TherapyPlanStatusRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

public record TherapyPlanStatusMapper(TherapyPlanStatusRepository statusLookup) {
    @Inject
    public TherapyPlanStatusMapper {
    }

    /**
     * Converts a TherapyPlanStatusEntity lookup entity to the TherapyPlanStatus domain enum.
     *
     * @param entity The persistence entity.
     * @return The domain enum.
     */
    public TherapyPlanStatus toDomain(TherapyPlanStatusEntity entity) {
        if (entity == null || entity.getName() == null) {
            return null;
        }

        return TherapyPlanStatus.valueOf(entity.getName().toUpperCase());
    }

    /**
     * Converts a TherapyPlanStatus domain enum to a TherapyPlanStatusEntity persistence entity.
     * This performs a lookup by the ENUM NAME to get the existing database record (FK).
     *
     * @param domain The domain enum.
     * @return The persistence entity reference.
     */
    public TherapyPlanStatusEntity toEntity(TherapyPlanStatus domain) {
        if (domain == null) {
            return null;
        }

        // Use the enum's string representation (e.g., "ACTIVE") to find the database row
        String statusName = domain.name();

        // Load the existing entity from the database using the injected lookup repository
        return statusLookup.findByName(statusName)
                .orElseThrow(() -> new EntityNotFoundException("TherapyPlan status not found for name: " + statusName));
    }
}
