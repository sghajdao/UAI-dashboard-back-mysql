package com.dashboard.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentsResponse {
    private Long user_id;
    private Long user_sis_id;
    private String name;
    private Integer courses_enrolled;
    private Integer courses_with_grade;
    private Double average_grade;
    private Long on_time_submissions;
    private Long missing_submissions;
    private Long late_submissions;
    private Long execused_submissions;
    private Integer since_last_attended;
    private Long since_last_activity;
    private List<Double> courses_score;
}
