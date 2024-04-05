package com.dashboard.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Canvas__context_modules {
    @Id
    private Long id;

    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private Date deleted_at;
    private Date created_at;
    private Date updated_at;
    private String workflow_state;
    @Column(nullable = true)
    private Date unlock_at;
    private Long context_id;
    private String context_type;
    @Column(nullable = true)
    private String migration_id;
    @Column(nullable = true)
    private String prerequisites;
    @Column(nullable = true)
    private String completion_requirements;
    @Column(nullable = true)
    private Boolean require_sequential_progress;
    @Column(nullable = true)
    private String completion_events;
    @Column(nullable = true)
    private Integer requirement_count;
    @Column(nullable = true)
    private Integer position;
}
