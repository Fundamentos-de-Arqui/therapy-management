package com.soulware.therapymanagement.domain.model.events;

import com.soulware.therapymanagement.domain.model.valueobjects.ids.SessionId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapymanagement.shared.domain.model.events.BaseAbstractDomainEvent;

public final class SessionTherapistReassignedEvent extends BaseAbstractDomainEvent {

    private final SessionId sessionId;
    private final TherapistId newTherapistId;

    /**
     * Constructs the SessionTherapistReassignedEvent.
     *
     * @param sessionId The ID of the session whose therapist was changed.
     * @param newTherapistId The ID of the therapist now assigned to the session.
     */
    public SessionTherapistReassignedEvent(SessionId sessionId, TherapistId newTherapistId) {
        super();

        if (sessionId == null) {
            throw new IllegalArgumentException("Session ID cannot be null.");
        }
        if (newTherapistId == null) {
            throw new IllegalArgumentException("New therapist ID cannot be null.");
        }

        this.sessionId = sessionId;
        this.newTherapistId = newTherapistId;
    }

    public SessionId getSessionId() {
        return this.sessionId;
    }

    public TherapistId getNewTherapistId() {
        return this.newTherapistId;
    }
}
