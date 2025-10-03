package com.soulware.therapymanagement.domain.model.entities;

import com.soulware.therapymanagement.domain.model.exceptions.sessions.*;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.SessionId;
import com.soulware.therapymanagement.domain.model.valueobjects.SessionStatus;
import com.soulware.therapymanagement.domain.model.valueobjects.TimeSlot;
import com.soulware.therapymanagement.shared.domain.model.entities.BaseAbstractEntity;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapistId;

/**
 * Represents a therapy session to be done.
 */
public class Session extends BaseAbstractEntity  {
    private TimeSlot slot;
    private TherapistId therapistId;
    private SessionStatus status;

    /**
     * Constructs a Session Entity.
     */
    public Session(SessionId id, TimeSlot slot, TherapistId therapistId, SessionStatus status) {
        super(id);

        if (slot == null) {
            throw new IllegalArgumentException("Time slot cannot be null.");
        }
        if (therapistId == null) {
            throw new IllegalArgumentException("Therapist ID cannot be null.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Session status cannot be null.");
        }

        this.slot = slot;
        this.therapistId = therapistId;
        this.status = status;
    }

    /**
     * Updates the time slot of the session.
     * @param slot The new TimeSlot.
     */
    public void reschedule(TimeSlot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("New time slot cannot be null.");
        }

        if (!this.status.canTransitionTo(SessionStatus.SCHEDULED)) {
            switch(this.status) {
                case DONE:
                    throw new CannotRescheduleCompletedSessionException("Cannot reschedule a session that is already done.");
                case CANCELED:
                    throw new SessionAlreadyCanceledException("Cannot reschedule a session that has been canceled.");
                case MISSED:
                    throw new CannotRescheduleCompletedSessionException("Cannot reschedule a session that was missed.");
                default:
                    throw new IllegalStateException("Session is in an invalid state for rescheduling: " + this.status);
            }
        }

        this.slot = slot;
        this.status = SessionStatus.SCHEDULED;
    }

    /**
     * Reassigns the therapist for the session.
     * @param replacementTherapistId The ID of the new therapist.
     */
    public void reassignTherapist(TherapistId replacementTherapistId) {
        if (replacementTherapistId == null) {
            throw new IllegalArgumentException("Replacement therapist ID cannot be null.");
        }

        if (!(this.status == SessionStatus.REQUIRES_RESCHEDULING || this.status == SessionStatus.SCHEDULED)) {
            switch(this.status) {
                case DONE:
                    throw new CannotReassignCompletedSessionException("Cannot reassign therapist for a session that is already done.");
                case CANCELED:
                    throw new CannotReassignCanceledSessionException("Cannot reassign therapist for a session that has been canceled.");
                case MISSED:
                    throw new CannotReassignMissedSessionException("Cannot reassign therapist for a session that was missed.");
                default:
                    throw new IllegalStateException("Session is in an invalid state for therapist reassignment: " + this.status);
            }
        }

        this.therapistId = replacementTherapistId;
    }

    /**
     * Marks the session as completed successfully.
     */
    public void complete() {
        if (!this.status.canTransitionTo(SessionStatus.DONE)) {
            switch(this.status) {
                case DONE:
                    throw new SessionAlreadyCompletedException("Session is already completed.");
                case CANCELED:
                    throw new CannotCompleteCanceledSessionException("Cannot complete a canceled session.");
                case MISSED:
                    throw new CannotCompleteMissedSessionException("Cannot complete a missed session.");
                default:
                    throw new IllegalStateException("Session is in an invalid state for completion: " + this.status);
            }
        }

        this.status = SessionStatus.DONE;
    }

    /**
     * Marks the session as canceled.
     */
    public void cancel() {
        if (!this.status.canTransitionTo(SessionStatus.CANCELED)) {
            switch(this.status) {
                case DONE:
                    throw new CannotCancelCompletedSessionException("Cannot cancel a completed session.");
                case CANCELED:
                    throw new SessionAlreadyCanceledException("Session is already canceled.");
                case MISSED:
                    throw new CannotCancelCompletedSessionException("Cannot cancel a missed session.");
                default:
                    throw new IllegalStateException("Session is in an invalid state for cancellation: " + this.status);
            }
        }

        this.status = SessionStatus.CANCELED;
    }

    /**
     * Marks the session as missed by the client.
     */
    public void miss() {
        if (!this.status.canTransitionTo(SessionStatus.MISSED)) {
            switch(this.status) {
                case DONE:
                    throw new CannotMissCompletedSessionException("Cannot mark a completed session as missed.");
                case CANCELED:
                    throw new CannotMissCanceledSessionException("Cannot mark a canceled session as missed.");
                case MISSED:
                    throw new CannotMissSessionException("Session is already marked as missed.");
                default:
                    throw new IllegalStateException("Session is in an invalid state to be marked as missed: " + this.status);
            }
        }

        this.status = SessionStatus.MISSED;
    }

    /**
     * Marks the session as requiring a rescheduling.
     */
    public void requestReschedule() {
        if (!this.status.canTransitionTo(SessionStatus.REQUIRES_RESCHEDULING)) {

            switch (this.status) {
                case DONE:
                    throw new CannotRescheduleCompletedSessionException("Cannot request reschedule for a session that is already done.");
                case CANCELED:
                    throw new SessionAlreadyCanceledException("Cannot request reschedule for a session that has been canceled.");
                case MISSED:
                    throw new CannotRescheduleCompletedSessionException("Cannot request reschedule for a session that was missed.");
                case REQUIRES_RESCHEDULING:
                    throw new SessionAlreadyRequiresRescheduleException("Session already requires rescheduling.");
                default:
                    throw new IllegalStateException("Session is in an invalid state to request rescheduling: " + this.status);
            }
        }

        this.status = SessionStatus.REQUIRES_RESCHEDULING;
    }
    
    // Getters
    public TimeSlot getSlot() { return this.slot; }
    public TherapistId getTherapistId() { return this.therapistId; }
    public SessionStatus getStatus() { return this.status; }

    @Override
    public SessionId getId() {
        return (SessionId) super.getId();
    }
}
