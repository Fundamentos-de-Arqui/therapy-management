package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class CannotRescheduleCompletedSessionException extends BaseAbstractDomainException {

    private static final String CODE = "SESSION_RESCHED_FINALIZED";

    public CannotRescheduleCompletedSessionException(String message) {
        super(message, CODE);
    }
}
