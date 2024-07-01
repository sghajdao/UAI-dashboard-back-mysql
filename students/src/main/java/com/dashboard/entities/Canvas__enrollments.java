package com.dashboard.entities;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Canvas__enrollments {

    private Long id;

    private Long sis_batch_id;
    private Long user_id;
    private Date created_at;
    private Date updated_at;
    private String workflow_state;
    private Long role_id;
    private Date start_at;
    private Date end_at;
    private Long course_id;
    private Date completed_at;
    private Long course_section_id;
    private String grade_publishing_status;
    private Long associated_user_id;
    private Boolean self_enrolled;
    private Boolean limit_privileges_to_course_section;
    private Date last_activity_at;
    private Integer total_activity_time;
    private Long sis_pseudonym_id;
    private Date last_attended_at;
    private String type;
}
