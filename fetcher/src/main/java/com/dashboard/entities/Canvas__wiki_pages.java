package com.dashboard.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Canvas__wiki_pages {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true)
    private Long user_id;
    private Date created_at;
    private Date updated_at;
    private String workflow_state;
    private Long context_id;
    private String context_type;
    @Column(nullable = true)
    private Long assignment_id;
    @Column(nullable = true)
    private String migration_id;
    @Column(nullable = true)
    private Long wiki_id;
    @Column(nullable = true)
    private Long old_assignment_id;
    @Column(nullable = true)
    private Date todo_date;
    @Column(nullable = true)
    private String editing_roles;
    @Column(nullable = true)
    private Date revised_at;
    @Column(nullable = true)
    private String body;
    @Column(nullable = true)
    private String url;
    @Column(nullable = true)
    private String title;
    private Boolean protected_editing;
    @Column(nullable = true)
    private Boolean could_be_locked;
}
