package com.soulware.therapymanagement.domain.model.events;

import com.soulware.therapymanagement.domain.model.valueobjects.ids.SessionId;
import com.soulware.therapymanagement.shared.domain.model.events.BaseAbstractDomainEvent;

/**
 * Domain Event fired when a session has been successfully marked as completed (DONE)
 * within the WeeklySessions aggregate.
 */
public final class SessionCompletedEvent extends BaseAbstractDomainEvent {
    private final SessionId sessionId;

    /**
     * Constructs the SessionCompletedEvent.
     *
     * @param sessionId The ID of the session that was completed.
     */
    public SessionCompletedEvent(SessionId sessionId) {
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
