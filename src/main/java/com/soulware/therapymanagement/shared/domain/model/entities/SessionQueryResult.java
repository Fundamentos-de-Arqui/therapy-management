package com.soulware.therapymanagement.shared.domain.model.entities;

import java.time.ZonedDateTime;

public record SessionQueryResult(
        Long id,
        Long therapistId,
        Long patientId,
        Long legalResponsibleId,
        ZonedDateTime startAt,
        ZonedDateTime endsAt,
        String status
) {}

