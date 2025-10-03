package com.soulware.therapymanagement.domain.repositories;

import com.soulware.therapymanagement.domain.model.aggregates.WeeklySessions;
import com.soulware.therapymanagement.domain.model.valueobjects.YearWeek;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.LegalResponsibleId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.WeeklySessionsId;

import java.util.List;
import java.util.Optional;

public interface WeeklySessionsRepository {

    /**
     * Finds the Weekly Sessions Aggregate Root by its unique identifier.
     * @param id The ID of the weekly schedule to retrieve.
     * @return An Optional containing the WeeklySessions aggregate if found.
     */
    Optional<WeeklySessions> findById(WeeklySessionsId id);

    /**
     * Saves or updates the state of the Weekly Sessions Aggregate Root.
     * @param weeklySessions The aggregate to persist.
     */
    void save(WeeklySessions weeklySessions);

    /**
     * Finds all WeeklySessions aggregates associated with a specific therapy plan.
     * @param therapyPlanId The ID of the plan.
     * @return A list of WeeklySessions aggregates.
     */
    List<WeeklySessions> findByTherapyPlanId(TherapyPlanId therapyPlanId);

    /**
     * Finds the WeeklySessions aggregate for a specific week and plan ID.
     * @param therapyPlanId The ID of the parent plan.
     * @param yearWeek The specific year and week number.
     * @return An Optional containing the unique WeeklySessions aggregate.
     */
    Optional<WeeklySessions> findByPlanAndWeek(TherapyPlanId therapyPlanId, YearWeek yearWeek);

    /**
     * Finds all WeeklySessions aggregates associated with a specific legal guardian, 
     * typically for presentation in the UI.
     * @param legalResponsibleId The ID of the guardian/responsible party.
     * @return A list of WeeklySessions aggregates.
     */
    List<WeeklySessions> findByLegalResponsibleId(LegalResponsibleId legalResponsibleId);

    /**
     * Finds all WeeklySessions aggregates associated with a specific patient across different weeks.
     * @param patientId The ID of the patient.
     * @return A list of WeeklySessions aggregates belonging to the patient.
     */
    List<WeeklySessions> findByPatientId(PatientId patientId);
}
