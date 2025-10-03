package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class SessionAlreadyCanceledException extends BaseAbstractDomainException {

    private static final String CODE = "SESSION_ALREADY_CANCELED";

    public SessionAlreadyCanceledException(String message) {
        super(message, CODE);
    }
}
