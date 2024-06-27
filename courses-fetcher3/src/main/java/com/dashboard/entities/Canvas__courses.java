package com.dashboard.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Canvas__courses {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true)
    private Long storage_quota;
    @Column(nullable = true)
    private String integration_id;
    @Column(nullable = true)
    private String lti_context_id;
    @Column(nullable = true)
    private Long sis_batch_id;
    private Date created_at;
    private Date updated_at;
    private String workflow_state;
    private Long account_id;
    @Column(nullable = true)
    private Long grading_standard_id;
    @Column(nullable = true)
    private Date start_at;
    @Column(nullable = true)
    private String sis_source_id;
    @Column(nullable = true)
    private String group_weighting_scheme;
    @Column(nullable = true)
    private Date conclude_at;
    @Column(nullable = true)
    private Boolean is_public;
    @Column(nullable = true)
    private Boolean allow_student_wiki_edits;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String syllabus_body;
    @Column(nullable = true)
    private String default_wiki_editing_roles;
    @Column(nullable = true)
    private Long wiki_id;
    private Boolean allow_student_organized_groups;
    @Column(nullable = true)
    private String course_code;
    @Column(nullable = true)
    private String default_view;
    @Column(nullable = true)
    private Long abstract_course_id;
    private Long enrollment_term_id;
    @Column(nullable = true)
    private Boolean open_enrollment;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String tab_configuration;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String turnitin_comments;
    @Column(nullable = true)
    private Boolean self_enrollment;
    @Column(nullable = true)
    private String license;
    @Column(nullable = true)
    private Boolean indexed;
    @Column(nullable = true)
    private Boolean restrict_enrollments_to_course_dates;
    @Column(nullable = true)
    private Long template_course_id;
    @Column(nullable = true)
    private Long replacement_course_id;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String public_description;
    @Column(nullable = true)
    private String self_enrollment_code;
    @Column(nullable = true)
    private Integer self_enrollment_limit;
    @Column(nullable = true)
    private Long turnitin_id;
    @Column(nullable = true)
    private Boolean show_announcements_on_home_page;
    @Column(nullable = true)
    private Integer home_page_announcement_limit;
    @Column(nullable = true)
    private Long latest_outcome_import_id;
    @Column(nullable = true)
    private String grade_passback_setting;
    private Boolean template;
    private Boolean homeroom_course;
    private Boolean sync_enrollments_from_homeroom;
    @Column(nullable = true)
    private Long homeroom_course_id;
    @Column(nullable = true)
    private String locale;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String time_zone;
    @Column(nullable = true)
    private String uuid;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String settings;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "course")
    // @JsonManagedReference
    // private Collection<Canvas__attachments> attachments;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "course")
    // @JsonManagedReference
    // private Collection<Context_modules> context_modules;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "course")
    // @JsonManagedReference
    // private Collection<Discussion_topics> discussion_topics;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "course")
    // @JsonManagedReference
    // private Collection<Web_conferences> web_conferences;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "course")
    // @JsonManagedReference
    // private Collection<Wiki_pages> wiki_pages;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "course")
    // @JsonManagedReference
    // private Collection<Enrollments> enrollments;
}
