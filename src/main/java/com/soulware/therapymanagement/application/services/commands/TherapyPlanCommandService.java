package com.soulware.therapymanagement.application.services.commands;

import com.soulware.therapymanagement.application.commands.CreateTherapyPlanCommand;
import com.soulware.therapymanagement.application.commands.ScheduleEntryCommand;
import com.soulware.therapymanagement.domain.model.aggregates.TherapyPlan;
import com.soulware.therapymanagement.domain.model.valueobjects.TherapyPlanStatus;
import com.soulware.therapymanagement.domain.model.valueobjects.TimeSlot;
import com.soulware.therapymanagement.domain.model.valueobjects.WeeklySchedule;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.*;
import com.soulware.therapymanagement.domain.repositories.TherapyPlanRepository;
import com.soulware.therapymanagement.infrastructure.events.cdi.CdiEventPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class TherapyPlanCommandService {
    private static final Logger logger = Logger.getLogger(TherapyPlanCommandService.class.getName());

    @Inject
    TherapyPlanRepository therapyPlanRepository;

    @Inject
    CdiEventPublisher eventPublisher;

    public TherapyPlan create(CreateTherapyPlanCommand command) {
        logger.info("[SERVICE] Starting create() with command = " + command);

        WeeklySchedule weeklySchedule = buildWeeklySchedule(command.schedule());

        logger.info("[SERVICE] Creating aggregate TherapyPlan...");
        TherapyPlan therapyPlan = new TherapyPlan(
                new TherapyPlanId(command.patientId()),
                eventPublisher,
                new TherapistId(command.assignedTherapistId()),
                new PatientId(command.patientId()),
                new LegalResponsibleId(command.legalResponsibleId()),
                TherapyPlanStatus.ACTIVE,
                weeklySchedule
        );

        logger.info("[SERVICE] Saving TherapyPlan to repository...");
        therapyPlanRepository.save(therapyPlan);

        logger.info("[SERVICE] Saved TherapyPlan with ID: " + therapyPlan.getId().value());
        return therapyPlan;
    }

    private WeeklySchedule buildWeeklySchedule(List<ScheduleEntryCommand> scheduleEntries) {
        if (scheduleEntries == null || scheduleEntries.isEmpty()) {
            return new WeeklySchedule(Collections.emptyMap());
        }

        Map<DayOfWeek, TimeSlot> scheduleMap = scheduleEntries.stream()
                .collect(Collectors.toMap(
                        entry -> {
                            try {
                                return DayOfWeek.valueOf(entry.dayOfWeek().toUpperCase());
                            } catch (IllegalArgumentException e) {
                                throw new IllegalArgumentException("Invalid day of week value in command: " + entry.dayOfWeek(), e);
                            }
                        },
                        entry -> new TimeSlot(entry.startTime(), entry.endTime()),
                        (existing, replacement) -> {
                            throw new IllegalArgumentException("Duplicate schedule entry detected for the same day.");
                        }
                ));

        return new WeeklySchedule(scheduleMap);
    }
}
