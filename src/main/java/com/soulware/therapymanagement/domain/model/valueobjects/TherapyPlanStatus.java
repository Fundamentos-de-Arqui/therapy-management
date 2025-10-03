package com.soulware.therapymanagement.domain.model.valueobjects;

import java.util.Set;

/**
 * TherapyPlanStatus value object.
 * Represents the current state of a therapeutic plan.
 */
public enum TherapyPlanStatus {
    /**
     * The plan is active and sessions are currently being conducted.
     */
    ACTIVE {
        @Override
        public Set<TherapyPlanStatus> nextStates() {
            return Set.of(CONCLUDED);
        }
    },

    /**
     * The plan has been successfully completed, archived, or terminated.
     */
    CONCLUDED;

    public Set<TherapyPlanStatus> nextStates() {
        return Set.of();
    }

    public boolean canTransitionTo(TherapyPlanStatus target) {
        return nextStates().contains(target);
    }
}
