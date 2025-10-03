package com.soulware.therapymanagement.domain.model.events;

import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapymanagement.shared.domain.model.events.BaseAbstractDomainEvent;

public final class TherapyPlanConcludedEvent extends BaseAbstractDomainEvent {

    private final TherapyPlanId planId;
    private final PatientId patientId;

    public TherapyPlanConcludedEvent(TherapyPlanId planId, PatientId patientId) {
        super();

        if (planId == null) {
            throw new IllegalArgumentException("Plan ID cannot be null.");
        }
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null.");
        }

        this.planId = planId;
        this.patientId = patientId;
    }

    public TherapyPlanId getPlanId() {
        return this.planId;
    }

    public PatientId getPatientId() {
        return this.patientId;
    }
}
