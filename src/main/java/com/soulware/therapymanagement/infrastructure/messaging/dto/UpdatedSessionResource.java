package com.soulware.therapymanagement.infrastructure.messaging.dto;

import java.time.ZonedDateTime;

public record UpdatedSessionResource(
        Long id,
        Long planId,
        String status,
        String day,
        ZonedDateTime startAt,
        ZonedDateTime endsAt
) {
}
