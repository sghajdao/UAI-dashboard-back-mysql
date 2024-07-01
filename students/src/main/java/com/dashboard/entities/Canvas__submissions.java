package com.dashboard.entities;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Canvas__submissions {

    private Long id;
    private Long attachment_id;
    private Long course_id;
    private Long user_id;
    private Date created_at;
    private Date updated_at;
    private String workflow_state;
    private Long assignment_id;
    private String media_comment_id;
    private String media_comment_type;
    private String attachment_ids;
    private Date posted_at;
    private Long group_id;
    private Double score;
    private Integer attempt;
    private Date submitted_at;
    private Long quiz_submission_id;
    private Integer extra_attempts;
    private Long grading_period_id;
    private String grade;
    private String submission_type;
    private Boolean processed;
    private Boolean grade_matches_current_submission;
    private Double published_score;
    private String published_grade;
    private Date graded_at;
    private Double student_entered_score;
    private Long grader_id;
    private Integer submission_comments_count;
    private Long media_object_id;
    private String turnitin_data;
    private Date cached_due_date;
    private Boolean excused;
    private Boolean graded_anonymously;
    private String late_policy_status;
    private Float points_deducted;
    private Long seconds_late_override;
    private String lti_user_id;
    private String anonymous_id;
    private Date last_comment_at;
    private Boolean cached_quiz_lti;
    private String cached_tardiness;
    private String resource_link_lookup_uuid;
    private Boolean redo_request;
    private String body;
    private String url;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JsonBackReference
    // private Canvas__users user;
}
