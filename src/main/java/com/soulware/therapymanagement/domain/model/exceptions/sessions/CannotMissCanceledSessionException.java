package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class CannotMissCanceledSessionException extends BaseAbstractDomainException {

    private static final String CODE = "SESSION_MISS_CANCELED";

    public CannotMissCanceledSessionException(String message) {
        super(message, CODE);
    }
}
