package com.soulware.therapymanagement.infrastructure.messaging.dto;

import java.time.ZonedDateTime;

public record SessionResource(
        Long id,
        Long therapistId,
        Long patientId,
        Long legalResponsibleId,
        ZonedDateTime startAt,
        ZonedDateTime endsAt,
        String status
) {
}
