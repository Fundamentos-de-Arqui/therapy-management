package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class CannotCompleteMissedSessionException extends BaseAbstractDomainException {

    private static final String CODE = "SESSION_COMPLETE_MISSED";

    public CannotCompleteMissedSessionException(String message) {
        super(message, CODE);
    }
}
