package com.soulware.therapymanagement.domain.model.events;

import com.soulware.therapymanagement.domain.model.valueobjects.ids.SessionId;
import com.soulware.therapymanagement.shared.domain.model.events.BaseAbstractDomainEvent;

/**
 * Domain Event fired when a session has been successfully canceled
 * within the WeeklySessions aggregate.
 */
public final class SessionCanceledEvent extends BaseAbstractDomainEvent {
    private final SessionId sessionId;

    /**
     * Constructs the SessionCanceledEvent.
     *
     * @param sessionId The ID of the session that was canceled.
     */
    public SessionCanceledEvent(SessionId sessionId) {
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
