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
public class Canvas__enrollments {
    @Id
    private Long id;

    @Column(nullable = true)
    private Long sis_batch_id;
    private Long user_id;
    private Date created_at;
    private Date updated_at;
    private String workflow_state;
    private Long role_id;
    @Column(nullable = true)
    private Date start_at;
    @Column(nullable = true)
    private Date end_at;
    private Long course_id;
    @Column(nullable = true)
    private Date completed_at;
    private Long course_section_id;
    private String grade_publishing_status;
    @Column(nullable = true)
    private Long associated_user_id;
    @Column(nullable = true)
    private Boolean self_enrolled;
    private Boolean limit_privileges_to_course_section;
    @Column(nullable = true)
    private Date last_activity_at;
    @Column(nullable = true)
    private Integer total_activity_time;
    @Column(nullable = true)
    private Long sis_pseudonym_id;
    @Column(nullable = true)
    private Date last_attended_at;
    @Column(name = "type")
    private String type;
}
