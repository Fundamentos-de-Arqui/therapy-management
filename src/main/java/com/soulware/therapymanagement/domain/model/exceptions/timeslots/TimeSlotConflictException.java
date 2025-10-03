package com.soulware.therapymanagement.domain.model.exceptions.timeslots;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class TimeSlotConflictException extends BaseAbstractDomainException {

    private static final String CODE = "SCHEDULE_TIME_CONFLICT";

    public TimeSlotConflictException(String message) {
        super(message, CODE);
    }
}
