package com.soulware.therapymanagement.infrastructure.persistence.jpa.repositories;

import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.SessionStatusEntity;

import java.util.Optional;

public interface SessionStatusRepository {
    Optional<SessionStatusEntity> findByName(String name);
}
