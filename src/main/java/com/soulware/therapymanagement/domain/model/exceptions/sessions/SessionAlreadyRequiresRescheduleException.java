package com.soulware.therapymanagement.domain.model.exceptions.sessions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class SessionAlreadyRequiresRescheduleException extends BaseAbstractDomainException {

    private static final String CODE = "SESSION_ALREADY_REQ_RESCHED";

    public SessionAlreadyRequiresRescheduleException(String message) {
        super(message, CODE);
    }
}
