package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class CannotCompleteCanceledSessionException extends BaseAbstractDomainException {

    private static final String CODE = "SESSION_COMPLETE_CANCELED";

    public CannotCompleteCanceledSessionException(String message) {
        super(message, CODE);
    }
}
