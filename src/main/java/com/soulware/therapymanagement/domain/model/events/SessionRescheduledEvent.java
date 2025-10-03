package com.soulware.therapymanagement.domain.model.events;

import com.soulware.therapymanagement.domain.model.valueobjects.TimeSlot;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.SessionId;
import com.soulware.therapymanagement.shared.domain.model.events.BaseAbstractDomainEvent;

/**
 * Domain Event fired when a session's time slot has been successfully moved
 * (rescheduled) within the WeeklySessions aggregate.
 */
public final class SessionRescheduledEvent extends BaseAbstractDomainEvent {

    private final SessionId sessionId;
    private final TimeSlot newTimeSlot;

    /**
     * Constructs the SessionRescheduledEvent.
     *
     * @param sessionId The ID of the session that was rescheduled.
     * @param newTimeSlot The new TimeSlot assigned to the session.
     */
    public SessionRescheduledEvent(SessionId sessionId, TimeSlot newTimeSlot) {
        super();

        if (sessionId == null) {
            throw new IllegalArgumentException("Session ID cannot be null.");
        }
        if (newTimeSlot == null) {
            throw new IllegalArgumentException("New TimeSlot cannot be null.");
        }

        this.sessionId = sessionId;
        this.newTimeSlot = newTimeSlot;
    }

    public SessionId getSessionId() {
        return this.sessionId;
    }

    public TimeSlot getNewTimeSlot() {
        return this.newTimeSlot;
    }
}
