package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class CannotMissCompletedSessionException extends BaseAbstractDomainException {

    private static final String CODE = "SESSION_MISS_COMPLETED";

    public CannotMissCompletedSessionException(String message) {
        super(message, CODE);
    }
}
