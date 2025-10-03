package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class CannotCancelCompletedSessionException extends BaseAbstractDomainException {

    private static final String CODE = "SESSION_CANCEL_FINALIZED";

    public CannotCancelCompletedSessionException(String message) {
        super(message, CODE);
    }
}
