package com.soulware.therapymanagement.domain.model.exceptions;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

/**
 * Exception thrown by the WeeklySessions Aggregate Root when an attempt is made to retrieve
 * a Session entity that does not exist within its current collection.
 * This indicates an attempt to access a non-existent child entity.
 */
public class SessionNotFoundException extends BaseAbstractDomainException {
    private static final String CODE = "SESSION_NOT_FOUND";

    /**
     * Constructs a SessionNotFoundException.
     *
     * @param message The detailed message explaining which session ID could not be found.
     */
    public SessionNotFoundException(String message) {
        super(message, CODE);
    }
}