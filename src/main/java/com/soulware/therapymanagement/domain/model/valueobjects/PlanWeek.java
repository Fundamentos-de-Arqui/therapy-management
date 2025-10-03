package com.soulware.therapymanagement.domain.model.valueobjects;

/**
 * PlanWeek value object.
 * Represents the execution counter of a therapy plan, starting from 1 (the first week).
 * Ensures the week counter is always positive.
 */
public record PlanWeek(int value) {
    private static final int MIN_WEEK_NUMBER = 1;

    /**
     * Constructs the PlanWeek value object.
     *
     * @param value The week number in the plan's execution cycle (must be 1 or greater).
     * @throws IllegalArgumentException if the week number is less than 1.
     */
    public PlanWeek {
        if (value < MIN_WEEK_NUMBER) {
            throw new IllegalArgumentException("Plan week number must be " + MIN_WEEK_NUMBER + " or greater.");
        }

    }

    /**
     * Creates a new PlanWeek representing the next week in the sequence.
     *
     * @return A new PlanWeek instance with a value incremented by one.
     */
    public PlanWeek nextWeek() {
        return new PlanWeek(this.value + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanWeek planWeek = (PlanWeek) o;
        return value == planWeek.value;
    }

    /**
     * Provides the string representation of the week counter value.
     */
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
