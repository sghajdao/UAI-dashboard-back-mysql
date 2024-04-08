package com.dashboard.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Canvas__web_conferences {
    @Id
    private Long id;

    private Long user_id;
    private Date created_at;
    private Date updated_at;
    private Long context_id;
    private String context_type;
    @Column(nullable = true)
    private Date start_at;
    @Column(nullable = true)
    private Date end_at;
    @Column(nullable = true)
    private String context_code;
    @Column(nullable = true)
    private Date started_at;
    @Column(nullable = true)
    private String user_ids;
    @Column(nullable = true)
    private Date ended_at;
    @Column(nullable = true)
    private Boolean recording_ready;
    private String conference_type;
    @Column(nullable = true)
    private String conference_key;
    @Column(nullable = true)
    private String description;
    @Column(nullable = true)
    private Double duration;
    @Column(nullable = true)
    private String settings;
    @Column(nullable = true)
    private String title;
    @Column(nullable = true)
    private String uuid;
}
