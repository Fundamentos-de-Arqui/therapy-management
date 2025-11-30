package com.soulware.therapymanagement.shared.infrastructure;


import com.soulware.therapymanagement.domain.model.entities.Session;
import com.soulware.therapymanagement.infrastructure.messaging.dto.SessionResource;
import com.soulware.therapymanagement.shared.domain.model.entities.SessionQueryResult;

public class SessionResourceAssembler {

    public static SessionResource toResource(SessionQueryResult entity) {
        if (entity == null) return null;

        return new SessionResource(
                entity.id(),
                entity.therapistId(),
                entity.patientId(),
                entity.legalResponsibleId(),
                entity.startAt(),
                entity.endsAt(),
                entity.status()
        );
    }
}

