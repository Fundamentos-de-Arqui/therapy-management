package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class SessionAlreadyCompletedException extends BaseAbstractDomainException {

    private static final String CODE = "SESSION_ALREADY_COMPLETED";

    public SessionAlreadyCompletedException(String message) {
        super(message, CODE);
    }
}
