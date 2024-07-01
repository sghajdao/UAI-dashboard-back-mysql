package com.dashboard.entities;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Canvas__scores {
    
    private Long id;

    private Date created_at;
    private Date updated_at;
    private String workflow_state;
    private Long assignment_group_id;
    private Long enrollment_id;
    private Long grading_period_id;
    private Double current_score;
    private Double final_score;
    private Boolean course_score;
    private Double unposted_current_score;
    private Double unposted_final_score;
    private Double current_points;
    private Double unposted_current_points;
    private Double final_points;
    private Double unposted_final_points;
    private Double override_score;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JsonBackReference
    // private Enrollments enrollment;
}
