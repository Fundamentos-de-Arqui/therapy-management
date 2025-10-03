package com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers;

import com.soulware.therapymanagement.domain.model.valueobjects.TimeSlot;
import com.soulware.therapymanagement.domain.model.valueobjects.WeeklySchedule;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.TherapyScheduleEntryEntity;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WeeklyScheduleMapper {
    public WeeklyScheduleMapper() {}

    /**
     * Converts a List of TherapyScheduleEntryEntity (persistence) to the WeeklySchedule domain record (Map).
     * @param entities The list of persistence entities.
     * @return The domain WeeklySchedule record.
     */
    public WeeklySchedule toDomain(List<TherapyScheduleEntryEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return new WeeklySchedule(Map.of());
        }

        Map<DayOfWeek, TimeSlot> scheduleMap = entities.stream()
                .collect(Collectors.toMap(
                        entity -> DayOfWeek.valueOf(entity.getDay()),
                        entity -> new TimeSlot(entity.getStartTime(), entity.getEndTime())
                ));

        return new WeeklySchedule(scheduleMap);
    }

    /**
     * Converts the WeeklySchedule domain record (Map) to a List of TherapyScheduleEntryEntity (persistence).
     * @param domain The domain WeeklySchedule record.
     * @param planId The FK ID of the parent TherapyPlan (required for the unidirectional relationship).
     * @return The list of persistence entities.
     */
    public List<TherapyScheduleEntryEntity> toEntity(WeeklySchedule domain, Long planId) {
        if (domain == null || domain.schedule().isEmpty()) {
            return List.of();
        }

        return domain.schedule().entrySet().stream()
                .map(entry -> {
                    TherapyScheduleEntryEntity entity = new TherapyScheduleEntryEntity();

                    entity.setPlanId(planId);
                    entity.setDay(entry.getKey().name());
                    entity.setStartTime(entry.getValue().start());
                    entity.setEndTime(entry.getValue().end());

                    return entity;
                })
                .collect(Collectors.toList());
    }
}
