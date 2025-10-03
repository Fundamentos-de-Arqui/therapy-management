package com.soulware.therapymanagement.infrastructure.persistence.jpa.mappers;

import com.soulware.therapymanagement.domain.model.aggregates.WeeklySessions;
import com.soulware.therapymanagement.domain.model.entities.Session;
import com.soulware.therapymanagement.domain.model.valueobjects.PlanWeek;
import com.soulware.therapymanagement.domain.model.valueobjects.YearWeek;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.LegalResponsibleId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.WeeklySessionsId;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.SessionEntity;
import com.soulware.therapymanagement.infrastructure.persistence.jpa.entities.WeeklySessionsEntity;
import com.soulware.therapymanagement.shared.domain.model.events.DomainEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeeklySessionsMapperTest {

    private static final Long TEST_PLAN_ID = 100L;
    private static final Long TEST_SESSION_ID = 50L;
    private static final String TEST_YEAR_WEEK = "2025-05";

    @Mock
    SessionMapper sessionMapper;

    @Mock
    DomainEventPublisher eventPublisher;

    @InjectMocks
    WeeklySessionsMapper mapper;

    @Test
    void toEntity_mapsAllVosToPrimitivesAndSetsChildren() {
        // ARRANGE
        WeeklySessionsId id = new WeeklySessionsId(TEST_SESSION_ID);
        PlanWeek planWeek = new PlanWeek(3);
        YearWeek yearWeek = YearWeek.fromFormattedString(TEST_YEAR_WEEK);
        TherapyPlanId therapyPlanId = new TherapyPlanId(TEST_PLAN_ID);
        LegalResponsibleId legalResponsibleId = new LegalResponsibleId(200L);

        // Create a list of sessions and stub the child mapper's response
        Session mockDomainSession = mock(Session.class);
        SessionEntity mockEntitySession = mock(SessionEntity.class);

        when(sessionMapper.toEntity(any(Session.class))).thenReturn(mockEntitySession);

        WeeklySessions domain = new WeeklySessions(
                id, eventPublisher, planWeek, yearWeek, therapyPlanId,
                List.of(mockDomainSession), legalResponsibleId
        );

        // ACT
        WeeklySessionsEntity entity = mapper.toEntity(domain);

        // ASSERT
        assertNotNull(entity, "The entity should not be null.");
        assertEquals(3, entity.getPlanWeekNumber(), "PlanWeek VO should map to int.");
        assertEquals(TEST_YEAR_WEEK, entity.getYearWeek(), "YearWeek VO should map to String.");
        assertEquals(TEST_PLAN_ID, entity.getTherapyPlanId(), "TherapyPlanId VO should map to Long.");

        // Assert collection mapping
        assertFalse(entity.getSessions().isEmpty(), "Session list should be mapped.");
        assertEquals(1, entity.getSessions().size());

        // Verify child mapper was called
        verify(sessionMapper, times(1)).toEntity(mockDomainSession);
    }

    @Test
    void toDomain_mapsPrimitivesToVosAndHandlesChildren() {
        // ARRANGE
        // Create a persistence entity (the input)
        WeeklySessionsEntity entity = new WeeklySessionsEntity();
        entity.setId(TEST_SESSION_ID);
        entity.setPlanWeekNumber(12);
        entity.setYearWeek(TEST_YEAR_WEEK);
        entity.setTherapyPlanId(100L);
        entity.setLegalResponsibleId(200L);

        // Create an entity session list and stub the child mapper's response
        SessionEntity mockEntitySession = mock(SessionEntity.class);
        entity.setSessions(List.of(mockEntitySession));

        Session mockDomainSession = mock(Session.class);
        when(sessionMapper.toDomain(any(SessionEntity.class))).thenReturn(mockDomainSession);

        // ACT
        WeeklySessions domain = mapper.toDomain(entity);

        // ASSERT
        assertNotNull(domain, "The domain aggregate should not be null.");
        assertEquals(TEST_SESSION_ID, domain.getId().value(), "ID should be mapped to WeeklySessionsId VO.");
        assertEquals(12, domain.getPlanWeek().value(), "PlanWeek VO should be created correctly.");
        assertEquals(TEST_YEAR_WEEK, domain.getYearWeek().toFormattedString(), "YearWeek VO should be created from formatted string.");
        assertEquals(TEST_PLAN_ID, domain.getTherapyPlanId().value(), "TherapyPlanId VO should be created correctly.");

        // Assert collection mapping
        assertFalse(domain.getSessions().isEmpty(), "Session list should be mapped and non-empty.");
        assertEquals(1, domain.getSessions().size());

        // Verify child mapper was called
        verify(sessionMapper, times(1)).toDomain(mockEntitySession);
    }
}