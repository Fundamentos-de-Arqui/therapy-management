package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class CannotReassignCompletedSessionException extends BaseAbstractDomainException {

    private static final String CODE = "THERAPIST_REASSIGN_DONE";

    public CannotReassignCompletedSessionException(String message) {
        super(message, CODE);
    }
}
