package com.soulware.therapymanagement.domain.model.exceptions.timeslots;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class InvalidTimeSlotIntervalException extends BaseAbstractDomainException {

    private static final String CODE = "TIMESLOT_INVALID_INTERVAL";

    public InvalidTimeSlotIntervalException(String message) {
        super(message, CODE);
    }
}
