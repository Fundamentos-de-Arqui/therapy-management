package com.soulware.therapymanagement.application.services.commands;

import com.soulware.therapymanagement.application.commands.UpdateSessionStatusCommand;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.TherapyScheduleEntryEntity;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.SessionStatusEntity;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers.SessionStatusMapper;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.repositories.JpaWeeklySessionsRepository;
import com.soulware.therapymanagement.domain.model.valueobjects.SessionStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class SessionCommandService {

    private static final Logger logger = Logger.getLogger(SessionCommandService.class.getName());

    @Inject
    JpaWeeklySessionsRepository weeklySessionsRepository;

    @Inject
    SessionStatusMapper sessionStatusMapper;

    @Transactional
    public TherapyScheduleEntryEntity updateSessionStatus(UpdateSessionStatusCommand command) {
        TherapyScheduleEntryEntity entry = weeklySessionsRepository.findEntryById(command.entryId())
                .orElseThrow(() -> new IllegalArgumentException("Schedule entry not found"));

        entry.setStatus(sessionStatusMapper.toEntity(SessionStatus.valueOf(command.status())));
        weeklySessionsRepository.saveEntry(entry);

        return entry;
    }

}
