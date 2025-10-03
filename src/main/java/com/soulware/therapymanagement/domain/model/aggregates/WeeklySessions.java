package com.soulware.therapymanagement.domain.model.aggregates;

import com.soulware.therapymanagement.domain.model.entities.Session;
import com.soulware.therapymanagement.domain.model.events.*;
import com.soulware.therapymanagement.domain.model.exceptions.SessionNotFoundException;
import com.soulware.therapymanagement.domain.model.exceptions.timeslots.TimeSlotConflictException;
import com.soulware.therapymanagement.domain.model.valueobjects.PlanWeek;
import com.soulware.therapymanagement.domain.model.valueobjects.TimeSlot;
import com.soulware.therapymanagement.domain.model.valueobjects.YearWeek;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.*;
import com.soulware.therapymanagement.shared.domain.model.aggregates.BaseAbstractAggregate;
import com.soulware.therapymanagement.shared.domain.model.events.DomainEventPublisher;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a collection of sessions grouped by a week to be done.
 * A week is defined as 6 days, starting on Monday, ending of Saturday.
 */
public class WeeklySessions extends BaseAbstractAggregate  {
    private final PlanWeek planWeek;
    private final YearWeek yearWeek;
    private final TherapyPlanId therapyPlanId;
    private final List<Session> sessions;
    private final LegalResponsibleId legalResponsibleId;

    /**
     * Constructs a Weekly Schedule Aggregate Root.
     */
    public WeeklySessions(WeeklySessionsId id, DomainEventPublisher eventPublisher, PlanWeek planWeek, YearWeek yearWeek, TherapyPlanId therapyPlanId, List<Session> sessions, LegalResponsibleId legalResponsibleId) {
        super(id, eventPublisher);

        if (planWeek == null) {
            throw new IllegalArgumentException("Time slot cannot be null.");
        }
        if (yearWeek == null) {
            throw new IllegalArgumentException("Therapist ID cannot be null.");
        }
        if (therapyPlanId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null.");
        }
        if (sessions == null) {
            throw new IllegalArgumentException("Sessions cannot be null.");
        }
        if (sessions.isEmpty()) {
            throw new IllegalArgumentException("Sessions list cannot be empty.");
        }
        if (legalResponsibleId == null) {
            throw new IllegalArgumentException("Legal responsible ID cannot be null.");
        }

        this.planWeek = planWeek;
        this.yearWeek = yearWeek;
        this.therapyPlanId = therapyPlanId;
        this.sessions = sessions;
        this.legalResponsibleId = legalResponsibleId;
    }

    /**
     * Reschedules a specific session within the week to a new time slot,
     * ensuring no conflicts occur with existing sessions.
     */
    public void rescheduleSessionTime(SessionId sessionId, TimeSlot newSlot) {
        if (newSlot == null) {
            throw new IllegalArgumentException("New time slot cannot be null.");
        }

        Session sessionToReschedule = getSessionById(sessionId);

        if (this.isOverlappingWithOthers(newSlot, sessionId)) {
            throw new TimeSlotConflictException("The new time slot conflicts with an existing session in the schedule.");
        }

        sessionToReschedule.reschedule(newSlot);

        publishEvent(new SessionRescheduledEvent(sessionId, newSlot));
    }

    /**
     * Executes the cancellation operation on a specific session.
     */
    public void cancelSession(SessionId sessionId) {
        Session sessionToCancel = getSessionById(sessionId);
        sessionToCancel.cancel();

        publishEvent(new SessionCanceledEvent(sessionId));
    }

    /**
     * Executes the completion operation on a specific session.
     */
    public void completeSession(SessionId sessionId) {
        Session sessionToComplete = getSessionById(sessionId);
        sessionToComplete.complete();

        publishEvent(new SessionCompletedEvent(sessionId));
    }

    /**
     * Executes the miss operation on a specific session.
     */
    public void missSession(SessionId sessionId) {
        Session sessionToMiss = getSessionById(sessionId);
        sessionToMiss.miss();

        publishEvent(new SessionMissedEvent(sessionId));
    }

    /**
     * Marks the session as requiring a rescheduling due to external factors (e.g., therapist unavailability).
     */
    public void requestRescheduleSession(SessionId sessionId) {
        Session sessionToRequestReschedule = getSessionById(sessionId);
        sessionToRequestReschedule.requestReschedule();

        publishEvent(new SessionRescheduleRequestedEvent(sessionId));
    }

    /**
     * Reassigns the therapist for the session.
     */
    public void reassignTherapist(SessionId sessionId, TherapistId replacementTherapistId) {
        Session sessionToReassign = getSessionById(sessionId);
        sessionToReassign.reassignTherapist(replacementTherapistId);

        publishEvent(new SessionTherapistReassignedEvent(sessionId, replacementTherapistId));
    }

    // Session utility functions

    /**
     * Finds a session entity within the aggregate by its ID.
     */
    public Session getSessionById(SessionId sessionId) {
        Optional<Session> session = this.sessions.stream()
                .filter(s -> s.getId().equals(sessionId))
                .findFirst();

        return session.orElseThrow(() -> new SessionNotFoundException("Session with ID " + sessionId.value() + " not found in this weekly schedule."));
    }

    /**
     * Checks if a proposed time slot conflicts with any other session in the aggregate,
     * excluding the session with the provided ID (used during updates).
     */
    private boolean isOverlappingWithOthers(TimeSlot proposedSlot, SessionId excludedId) {
        return this.sessions.stream()
                .filter(s -> !s.getId().equals(excludedId))
                .anyMatch(s -> s.getSlot().overlaps(proposedSlot));
    }

    // Getters
    public PlanWeek getPlanWeek() { return this.planWeek; }
    public YearWeek getYearWeek() { return this.yearWeek; }
    public TherapyPlanId getTherapyPlanId() { return this.therapyPlanId; }
    public LegalResponsibleId getLegalResponsibleId() { return this.legalResponsibleId; }

    /**
     * Provides an unmodifiable view of the internal sessions list.
     * This prevents external code from directly modifying the aggregate's state.
     * @return An unmodifiable List of sessions.
     */
    public List<Session> getSessions() {
        return Collections.unmodifiableList(this.sessions);
    }

    @Override
    public WeeklySessionsId getId() {
        return (WeeklySessionsId) super.getId();
    }
}
