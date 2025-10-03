package com.soulware.therapymanagement.domain.model.events;

import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.shared.domain.model.events.BaseAbstractDomainEvent;

public final class TherapyPlanTherapistReassignedEvent extends BaseAbstractDomainEvent {

    private final TherapyPlanId planId;
    private final TherapistId newTherapistId;

    /**
     * Constructs the TherapyPlanTherapistReassignedEvent.
     *
     * @param planId The ID of the plan affected by the reassignment.
     * @param newTherapistId The ID of the therapist now responsible for the plan.
     */
    public TherapyPlanTherapistReassignedEvent(TherapyPlanId planId, TherapistId newTherapistId) {
        super();

        if (planId == null) {
            throw new IllegalArgumentException("Plan ID cannot be null.");
        }
        if (newTherapistId == null) {
            throw new IllegalArgumentException("New therapist ID cannot be null.");
        }

        this.planId = planId;
        this.newTherapistId = newTherapistId;
    }

    public TherapyPlanId getPlanId() {
        return this.planId;
    }

    public TherapistId getNewTherapistId() {
        return this.newTherapistId;
    }
}
