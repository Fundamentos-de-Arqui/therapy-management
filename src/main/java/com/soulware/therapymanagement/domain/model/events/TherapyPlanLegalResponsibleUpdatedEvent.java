package com.soulware.therapymanagement.domain.model.events;

import com.soulware.therapymanagement.domain.model.valueobjects.ids.LegalResponsibleId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.shared.domain.model.events.BaseAbstractDomainEvent;

public final class TherapyPlanLegalResponsibleUpdatedEvent extends BaseAbstractDomainEvent {

    private final TherapyPlanId planId;
    private final LegalResponsibleId newLegalResponsibleId;

    /**
     * Constructs the TherapyPlanLegalResponsibleUpdatedEvent.
     *
     * @param planId The ID of the plan affected by the update.
     * @param newLegalResponsibleId The ID of the new legal responsible.
     */
    public TherapyPlanLegalResponsibleUpdatedEvent(TherapyPlanId planId, LegalResponsibleId newLegalResponsibleId) {
        super();

        if (planId == null) {
            throw new IllegalArgumentException("Plan ID cannot be null.");
        }
        if (newLegalResponsibleId == null) {
            throw new IllegalArgumentException("New legal responsible ID cannot be null.");
        }

        this.planId = planId;
        this.newLegalResponsibleId = newLegalResponsibleId;
    }

    public TherapyPlanId getPlanId() {
        return this.planId;
    }

    public LegalResponsibleId getNewLegalResponsibleId() {
        return this.newLegalResponsibleId;
    }
}
