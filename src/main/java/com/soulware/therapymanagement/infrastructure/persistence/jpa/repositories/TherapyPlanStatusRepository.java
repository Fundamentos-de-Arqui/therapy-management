package com.soulware.therapymanagement.infrastructure.persistence.jpa.repositories;

import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.TherapyPlanStatusEntity;

import java.util.Optional;

public interface TherapyPlanStatusRepository {
    Optional<TherapyPlanStatusEntity> findByName(String name);
}
