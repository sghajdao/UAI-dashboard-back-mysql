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
public class Canvas__submissions {

    @Id
    private Long id;
    @Column(nullable = true)
    private Long attachment_id;
    private Long course_id;
    private Long user_id;
    @Column(nullable = true)
    private Date created_at;
    @Column(nullable = true)
    private Date updated_at;
    private String workflow_state;
    private Long assignment_id;
    @Column(nullable = true)
    private String media_comment_id;
    @Column(nullable = true)
    private String media_comment_type;
    @Column(nullable = true)
    private String attachment_ids;
    @Column(nullable = true)
    private Date posted_at;
    @Column(nullable = true)
    private Long group_id;
    @Column(nullable = true)
    private Double score;
    @Column(nullable = true)
    private Integer attempt;
    @Column(nullable = true)
    private Date submitted_at;
    @Column(nullable = true)
    private Long quiz_submission_id;
    @Column(nullable = true)
    private Integer extra_attempts;
    @Column(nullable = true)
    private Long grading_period_id;
    @Column(nullable = true)
    private String grade;
    @Column(nullable = true)
    private String submission_type;
    @Column(nullable = true)
    private Boolean processed;
    @Column(nullable = true)
    private Boolean grade_matches_current_submission;
    @Column(nullable = true)
    private Double published_score;
    @Column(nullable = true)
    private String published_grade;
    @Column(nullable = true)
    private Date graded_at;
    @Column(nullable = true)
    private Double student_entered_score;
    @Column(nullable = true)
    private Long grader_id;
    @Column(nullable = true)
    private Integer submission_comments_count;
    @Column(nullable = true)
    private Long media_object_id;
    @Column(nullable = true)
    private String turnitin_data;
    @Column(nullable = true)
    private Date cached_due_date;
    @Column(nullable = true)
    private Boolean excused;
    @Column(nullable = true)
    private Boolean graded_anonymously;
    @Column(nullable = true)
    private String late_policy_status;
    @Column(nullable = true)
    private Float points_deducted;
    @Column(nullable = true)
    private Long seconds_late_override;
    @Column(nullable = true)
    private String lti_user_id;
    @Column(nullable = true)
    private String anonymous_id;
    @Column(nullable = true)
    private Date last_comment_at;
    private Boolean cached_quiz_lti;
    @Column(nullable = true)
    private String cached_tardiness;
    @Column(nullable = true)
    private String resource_link_lookup_uuid;
    private Boolean redo_request;
    @Column(nullable = true)
    private String body;
    @Column(nullable = true)
    private String url;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JsonBackReference
    // private Canvas__users user;
}
