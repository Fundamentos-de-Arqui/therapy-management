package com.soulware.therapymanagement.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "session_statuses")
public class SessionStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 20, unique = true) // VARCHAR(20) for the name
    private String name;

    // Required by JPA
    public SessionStatusEntity() {}

    // Setters and Getters
    public Long getId() { return id; }
    public void setId(Long statusId) { this.id = statusId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
