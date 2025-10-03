package com.soulware.therapymanagement.domain.model.exceptions.timeslots;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class InvalidDayOfWeekException extends BaseAbstractDomainException {

    private static final String CODE = "TIMESLOT_RULE_VIOLATION";

    /**
     * Constructs a InvalidDayOfWeekException.
     *
     * @param message The detailed message explaining which rule was violated (e.g., the day of week).
     */
    public InvalidDayOfWeekException(String message) {
        super(message, CODE);
    }
}
