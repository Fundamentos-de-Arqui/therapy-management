package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class CannotReassignMissedSessionException extends BaseAbstractDomainException {

    private static final String CODE = "THERAPIST_REASSIGN_MISSED";

    public CannotReassignMissedSessionException(String message) {
        super(message, CODE);
    }
}
