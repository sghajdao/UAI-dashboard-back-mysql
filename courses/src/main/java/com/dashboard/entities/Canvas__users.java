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
public class Canvas__users {

    @Id
    private Long id;
    @Column(nullable = true)
    private Date deleted_at;
    @Column(nullable = true)
    private Long storage_quota;
    @Column(nullable = true)
    private String lti_context_id;
    private Date created_at;
    private Date updated_at;
    private String workflow_state;
    @Column(nullable = true)
    private String sortable_name;
    @Column(nullable = true)
    private String avatar_image_url;
    @Column(nullable = true)
    private String avatar_image_source;
    @Column(nullable = true)
    private Date avatar_image_updated_at;
    @Column(nullable = true)
    private String short_name;
    @Column(nullable = true)
    private Date last_logged_out;
    @Column(nullable = true)
    private String pronouns;
    @Column(nullable = true)
    private Long merged_into_user_id;
    @Column(nullable = true)
    private String locale;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String time_zone;
    @Column(nullable = true)
    private String uuid;
    @Column(nullable = true)
    private String school_name;
    @Column(nullable = true)
    private String school_position;
    @Column(nullable = true, name = "public")
    private Boolean _public;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    // @JsonManagedReference
    // private Collection<Enrollments> enrollments;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    // @JsonManagedReference
    // private Collection<Submissions> submissions;
}
