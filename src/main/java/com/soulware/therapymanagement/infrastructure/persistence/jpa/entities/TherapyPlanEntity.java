package com.soulware.therapymanagement.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "therapy_plans")
public class TherapyPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assigned_therapist_id", nullable = false)
    private Long assignedTherapistId;

    @Column(name = "patient_id", nullable = false, updatable = false)
    private Long patientId;

    @Column(name = "legal_responsible_id", nullable = false)
    private Long legalResponsibleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private TherapyPlanStatusEntity status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "therapy_plan_id")
    private List<TherapyScheduleEntryEntity> scheduleEntries;

    // Audit
    @Column(name = "created_at", updatable = false, nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private ZonedDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
        this.modifiedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = ZonedDateTime.now();
    }

    // Required by JPA
    public TherapyPlanEntity() {}

    // Setters and Getters
    public Long getId() { return id; }
    public void setId(Long planId) { this.id = planId; }
    public Long getAssignedTherapistId() { return assignedTherapistId; }
    public void setAssignedTherapistId(Long assignedTherapistId) { this.assignedTherapistId = assignedTherapistId; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getLegalResponsibleId() { return legalResponsibleId; }
    public void setLegalResponsibleId(Long legalResponsibleId) { this.legalResponsibleId = legalResponsibleId; }
    public TherapyPlanStatusEntity getStatus() { return status; }
    public void setStatus(TherapyPlanStatusEntity status) { this.status = status; }
    public List<TherapyScheduleEntryEntity> getWeeklySchedule() { return scheduleEntries; }
    public void setScheduleLayout(List<TherapyScheduleEntryEntity> scheduleEntries) { this.scheduleEntries = scheduleEntries; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(ZonedDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
}
