package com.dashboard.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Canvas__scores {
    @Id
    private Long id;

    @Column(nullable = true)
    private Date created_at;
    @Column(nullable = true)
    private Date updated_at;
    private String workflow_state;
    @Column(nullable = true)
    private Long assignment_group_id;
    private Long enrollment_id;
    @Column(nullable = true)
    private Long grading_period_id;
    @Column(nullable = true)
    private Double current_score;
    @Column(nullable = true)
    private Double final_score;
    private Boolean course_score;
    @Column(nullable = true)
    private Double unposted_current_score;
    @Column(nullable = true)
    private Double unposted_final_score;
    @Column(nullable = true)
    private Double current_points;
    @Column(nullable = true)
    private Double unposted_current_points;
    @Column(nullable = true)
    private Double final_points;
    @Column(nullable = true)
    private Double unposted_final_points;
    @Column(nullable = true)
    private Double override_score;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JsonBackReference
    // private Enrollments enrollment;
}
