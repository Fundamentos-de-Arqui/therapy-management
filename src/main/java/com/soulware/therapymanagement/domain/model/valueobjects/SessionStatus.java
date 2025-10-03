package com.soulware.therapymanagement.domain.model.valueobjects;

import java.util.Set;

/**
 * SessionStatus value object.
 * Represents the status of a session (e.g. SCHEDULED, DONE, CANCELED).
 */
public enum SessionStatus {
    /**
     * The session has been scheduled and is upcoming.
     */
    SCHEDULED {
        @Override
        public Set<SessionStatus> nextStates() {
            return Set.of(DONE, CANCELED, MISSED);
        }
    },

    /**
     * The session programming falls on an un valid date (like a holiday) and must be rebooked for a future time slot.
     */
    REQUIRES_RESCHEDULING {
        @Override
        public Set<SessionStatus> nextStates() {
            return Set.of(SCHEDULED, CANCELED);
        }
    },

    /**
     * The session was successfully completed.
     */
    DONE,

    /**
     * The session was canceled by the therapist or the client before it started.
     */
    CANCELED,

    /**
     * The session was neither completed nor canceled.
     * Either the client or the assigned therapist did not attend or reach the session at the scheduled time.
     */
    MISSED;

    public Set<SessionStatus> nextStates() {
        return Set.of();
    }

    public boolean canTransitionTo(SessionStatus target) {
        return nextStates().contains(target);
    }
}
