package com.soulware.therapymanagement.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "weekly_sessions")
public class WeeklySessionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_week_number", nullable = false)
    private int planWeekNumber;

    @Column(name = "year_week", nullable = false)
    private String yearWeek;

    @Column(name = "therapy_plan_id", nullable = false)
    private Long therapyPlanId;

    @Column(name = "legal_responsible_id", nullable = false)
    private Long legalResponsibleId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "weekly_schedule_id", referencedColumnName = "id")
    private List<SessionEntity>  sessions;

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
    public WeeklySessionsEntity() {}

    // Setters and Getters
    public Long getId() { return id; }
    public void setId(Long weeklyScheduleId) { this.id = weeklyScheduleId; }
    public int getPlanWeekNumber() { return planWeekNumber; }
    public void setPlanWeekNumber(int planWeekNumber) { this.planWeekNumber = planWeekNumber; }
    public String getYearWeek() { return yearWeek; }
    public void setYearWeek(String yearWeek) { this.yearWeek = yearWeek; }
    public Long getTherapyPlanId() { return therapyPlanId; }
    public void setTherapyPlanId(Long therapyPlanId) { this.therapyPlanId = therapyPlanId; }
    public Long getLegalResponsibleId() { return legalResponsibleId; }
    public void setLegalResponsibleId(Long legalResponsibleId) { this.legalResponsibleId = legalResponsibleId; }
    public List<SessionEntity> getSessions() { return sessions; }
    public void setSessions(List<SessionEntity> sessions) { this.sessions = sessions; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(ZonedDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
}
