package com.soulware.therapymanagement.application.services.queries;

import com.soulware.therapymanagement.application.queries.GetSessionsQuery;
import com.soulware.therapymanagement.domain.repositories.WeeklySessionsRepository;
import com.soulware.therapymanagement.shared.domain.model.entities.SessionQueryResult;
import com.soulware.therapymanagement.shared.infrastructure.PagedResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SessionQueryService {
    @Inject
    WeeklySessionsRepository  weeklySessionsRepository;

    public PagedResult<SessionQueryResult> getSessions(GetSessionsQuery query) {
        return weeklySessionsRepository.findByFilters(
                query.therapistId(),
                query.responsibleLegalId(),
                query.patientId(),
                query.status(),
                query.page(),
                query.size()
        );
    }
}
