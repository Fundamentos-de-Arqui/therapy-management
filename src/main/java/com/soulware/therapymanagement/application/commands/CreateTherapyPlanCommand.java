package com.soulware.therapymanagement.application.commands;

import java.util.List;

public record CreateTherapyPlanCommand(
        Long assignedTherapistId,
        Long patientId,
        Long legalResponsibleId,
        List<ScheduleEntryCommand> schedule
) {
}
