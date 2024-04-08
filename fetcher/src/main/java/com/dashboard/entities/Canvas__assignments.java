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
@NoArgsConstructor
@AllArgsConstructor
public class Canvas__assignments {
    @Id
    private Long id;

    @Column(nullable = true)
    String integration_id;
    @Column(nullable = true, columnDefinition = "TEXT")
    String lti_context_id;
    @Column(nullable = true)
    Date created_at;
    @Column(nullable = true)
    Date updated_at;
    String workflow_state;
    @Column(nullable = true)
    Date due_at;
    @Column(nullable = true)
    Date unlock_at;
    @Column(nullable = true)
    Date lock_at;
    @Column(nullable = true)
    Double points_possible;
    @Column(nullable = true)
    String grading_type;
    String submission_types;
    @Column(nullable = true)
    Long assignment_group_id;
    @Column(nullable = true)
    Long grading_standard_id;
    Integer submissions_downloads;
    Integer peer_review_count;
    @Column(nullable = true)
    Date peer_reviews_due_at;
    Boolean peer_reviews_assigned;
    Boolean peer_reviews;
    Long context_id;
    String context_type;
    Boolean automatic_peer_reviews;
    Boolean all_day;
    @Column(nullable = true)
    Date all_day_date;
    Boolean could_be_locked;
    @Column(nullable = true)
    String migration_id;
    Boolean grade_group_students_individually;
    Boolean anonymous_peer_reviews;
    Boolean turnitin_enabled;
    @Column(nullable = true)
    String allowed_extensions;
    @Column(nullable = true)
    Long group_category_id;
    Boolean freeze_on_copy;
    Boolean only_visible_to_overrides;
    Boolean post_to_sis;
    Boolean moderated_grading;
    @Column(nullable = true)
    Date grades_published_at;
    Boolean omit_from_final_grade;
    Boolean intra_group_peer_reviews;
    Boolean vericite_enabled;
    Boolean anonymous_instructor_annotations;
    @Column(nullable = true)
    Long duplicate_of_id;
    Boolean anonymous_grading;
    Boolean graders_anonymous_to_graders;
    Integer grader_count;
    Boolean grader_comments_visible_to_graders;
    @Column(nullable = true)
    Long grader_section_id;
    @Column(nullable = true)
    Long final_grader_id;
    Boolean grader_names_visible_to_final_grader;
    @Column(nullable = true)
    Integer allowed_attempts;
    @Column(nullable = true, columnDefinition = "TEXT")
    String sis_source_id;
    @Column(nullable = true)
    Long annotatable_attachment_id;
    Boolean important_dates;
    @Column(nullable = true, columnDefinition = "TEXT")
    String description;
    @Column(nullable = true)
    Integer position;
    @Column(nullable = true)
    String title;
    @Column(nullable = true, columnDefinition = "TEXT")
    String turnitin_settings;
}
