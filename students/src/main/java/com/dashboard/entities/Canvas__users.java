package com.dashboard.entities;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Canvas__users {

    private Long id;
    private Date deleted_at;
    private Long storage_quota;
    private String lti_context_id;
    private Date created_at;
    private Date updated_at;
    private String workflow_state;
    private String sortable_name;
    private String avatar_image_url;
    private String avatar_image_source;
    private Date avatar_image_updated_at;
    private String short_name;
    private Date last_logged_out;
    private String pronouns;
    private Long merged_into_user_id;
    private String locale;
    private String name;
    private String time_zone;
    private String uuid;
    private String school_name;
    private String school_position;
    private Boolean _public;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    // @JsonManagedReference
    // private Collection<Enrollments> enrollments;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    // @JsonManagedReference
    // private Collection<Submissions> submissions;
}
