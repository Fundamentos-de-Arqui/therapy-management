package com.soulware.therapymanagement.domain.model.aggregates;

import com.soulware.therapymanagement.domain.model.events.TherapyPlanConcludedEvent;
import com.soulware.therapymanagement.domain.model.events.TherapyPlanLegalResponsibleUpdatedEvent;
import com.soulware.therapymanagement.domain.model.events.TherapyPlanTherapistReassignedEvent;
import com.soulware.therapymanagement.domain.model.exceptions.therapyplan.TherapyPlanAlreadyConcludedException;
import com.soulware.therapymanagement.domain.model.valueobjects.TherapyPlanStatus;
import com.soulware.therapymanagement.domain.model.valueobjects.WeeklySchedule;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.LegalResponsibleId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapymanagement.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapymanagement.shared.domain.model.aggregates.BaseAbstractAggregate;
import com.soulware.therapymanagement.shared.domain.model.events.DomainEventPublisher;

public class TherapyPlan extends BaseAbstractAggregate  {
    private TherapistId assignedTherapistId;
    private final PatientId patientId;
    private LegalResponsibleId legalResponsibleId;
    private TherapyPlanStatus status;
    private WeeklySchedule schedule;

    /**
     * Creates a therapy plan aggregate.
     */
    public TherapyPlan(TherapyPlanId id, DomainEventPublisher eventPublisher, TherapistId assignedTherapistId, PatientId patientId, LegalResponsibleId legalResponsibleId, TherapyPlanStatus status, WeeklySchedule schedule) {
        super(id, eventPublisher);

        if (assignedTherapistId == null) {
            throw new IllegalArgumentException("Assigned therapist ID cannot be null.");
        }
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null.");
        }
        if (legalResponsibleId == null) {
            throw new IllegalArgumentException("Legal responsible ID cannot be null.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Therapy plan status cannot be null.");
        }
        if (schedule == null) {
            throw new IllegalArgumentException("Weekly schedule cannot be null.");
        }

        this.assignedTherapistId = assignedTherapistId;
        this.patientId = patientId;
        this.legalResponsibleId = legalResponsibleId;
        this.status = status;
        this.schedule = schedule;
    }

    public void concludePlan() {
        if (this.status == TherapyPlanStatus.CONCLUDED) {
            throw new TherapyPlanAlreadyConcludedException("Therapy plan is already concluded.");
        }

        this.status = TherapyPlanStatus.CONCLUDED;

        publishEvent(new TherapyPlanConcludedEvent(this.getId(), this.patientId));
    }

    public void updateLegalResponsible(LegalResponsibleId newLegalResponsibleId) {
        if (newLegalResponsibleId == null) {
            throw new IllegalArgumentException("New legal responsible ID cannot be null.");
        }
        if (this.status == TherapyPlanStatus.CONCLUDED) {
            throw new TherapyPlanAlreadyConcludedException("Cannot update legal responsible for a concluded plan.");
        }

        this.legalResponsibleId = newLegalResponsibleId;
        publishEvent(new TherapyPlanLegalResponsibleUpdatedEvent(this.getId(), newLegalResponsibleId));
    }

    public void reassignTherapist(TherapistId newAssignedTherapistId) {
        if (newAssignedTherapistId == null) {
            throw new IllegalArgumentException("Assigned therapist ID cannot be null.");
        }
        if (this.status == TherapyPlanStatus.CONCLUDED) {
            throw new TherapyPlanAlreadyConcludedException("Cannot reassign therapist for a concluded plan.");
        }

        this.assignedTherapistId = newAssignedTherapistId;
        publishEvent(new TherapyPlanTherapistReassignedEvent(this.getId(), newAssignedTherapistId));
    }
    
    public void updateWeeklySchedule(WeeklySchedule newSchedule) {
        if (newSchedule == null) {
            throw new IllegalArgumentException("Weekly schedule cannot be null.");
        }

        this.schedule = newSchedule;
    }

    // Getters
    public TherapistId getAssignedTherapistId() { return this.assignedTherapistId; }
    public PatientId getPatientId() { return this.patientId; }
    public LegalResponsibleId getLegalResponsibleId() { return this.legalResponsibleId; }
    public TherapyPlanStatus getStatus() { return this.status; }
    public WeeklySchedule getSchedule() { return this.schedule; }

    @Override
    public TherapyPlanId getId() {
        return (TherapyPlanId) super.getId();
    }
}
