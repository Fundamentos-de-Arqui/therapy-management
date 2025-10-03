package com.soulware.therapymanagement.domain.model.events;

import com.soulware.therapymanagement.domain.model.valueobjects.ids.SessionId;
import com.soulware.therapymanagement.shared.domain.model.events.BaseAbstractDomainEvent;

/**
 * Domain Event fired when a request is made to reschedule a session, setting its status
 * to REQUIRES_RESCHEDULING due to a conflict or external factor.
 */
public final class SessionRescheduleRequestedEvent extends BaseAbstractDomainEvent {
    private final SessionId sessionId;

    /**
     * Constructs the SessionRescheduleRequestedEvent.
     *
     * @param sessionId The ID of the session for which rescheduling was requested.
     */
    public SessionRescheduleRequestedEvent(SessionId sessionId) {
        super();

        if (sessionId == null) {
            throw new IllegalArgumentException("Session ID cannot be null.");
        }

        this.sessionId = sessionId;
    }

    public SessionId getSessionId() {
        return this.sessionId;
    }
}
