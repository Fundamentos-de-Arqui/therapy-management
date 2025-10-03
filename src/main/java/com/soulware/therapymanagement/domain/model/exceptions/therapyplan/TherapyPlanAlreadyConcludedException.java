package com.soulware.therapymanagement.domain.model.exceptions.therapyplan;

import com.soulware.therapymanagement.shared.domain.model.exceptions.BaseAbstractDomainException;

public class TherapyPlanAlreadyConcludedException extends BaseAbstractDomainException {

    private static final String CODE = "PLAN_ALREADY_CONCLUDED";

    public TherapyPlanAlreadyConcludedException(String message) {
        super(message, CODE);
    }
}
