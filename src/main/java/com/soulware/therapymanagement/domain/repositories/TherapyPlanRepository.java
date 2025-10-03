package com.soulware.therapymanagement.domain.repositories;

import com.soulware.therapymanagement.domain.model.aggregates.TherapyPlan;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;

import java.util.List;
import java.util.Optional;

public interface TherapyPlanRepository {

    /**
     * Finds the Therapy Plan Aggregate Root by its unique identifier.
     * @param id The ID of the plan to retrieve.
     * @return An Optional containing the TherapyPlan if found.
     */
    Optional<TherapyPlan> findById(TherapyPlanId id);

    /**
     * Saves or updates the state of the Therapy Plan Aggregate Root.
     * @param plan The TherapyPlan aggregate to persist.
     */
    void save(TherapyPlan plan);

    /**
     * Finds all therapy plans associated with a specific therapist.
     * @param therapistId The ID of the therapist whose plans are requested.
     * @return A list of TherapyPlan aggregates.
     */
    List<TherapyPlan> findByAssignedTherapistId(TherapistId therapistId);

    /**
     * Finds the active therapy plan associated with a specific patient.
     * As a patient should generally have only one active plan, this returns an Optional.
     * @param patientId The ID of the patient.
     * @return An Optional containing the active TherapyPlan.
     */
    Optional<TherapyPlan> findActivePlanByPatientId(PatientId patientId);
}
