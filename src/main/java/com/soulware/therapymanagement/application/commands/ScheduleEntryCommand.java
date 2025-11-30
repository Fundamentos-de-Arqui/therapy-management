package com.soulware.therapymanagement.application.commands;

import java.time.ZonedDateTime;

public record ScheduleEntryCommand(
        String dayOfWeek,
        ZonedDateTime startTime,
        ZonedDateTime endTime
) {
}