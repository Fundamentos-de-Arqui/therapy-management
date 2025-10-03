package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class CannotReassignCanceledSessionException extends BaseAbstractDomainException {

    private static final String CODE = "THERAPIST_REASSIGN_CANCELED";

    public CannotReassignCanceledSessionException(String message) {
        super(message, CODE);
    }
}
