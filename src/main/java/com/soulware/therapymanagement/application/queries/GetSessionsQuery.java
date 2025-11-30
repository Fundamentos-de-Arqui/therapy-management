package com.soulware.therapymanagement.application.queries;

import jakarta.validation.constraints.Null;

public record GetSessionsQuery(
        @Null Long patientId,
        @Null Long therapistId,
        @Null Long responsibleLegalId,
        @Null String status,
        int page,
        int size
) {
}
