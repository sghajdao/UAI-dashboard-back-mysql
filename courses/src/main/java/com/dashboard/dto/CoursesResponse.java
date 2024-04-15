package com.dashboard.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursesResponse {
    private Long id;
    private String name;
    private String teacher;
    private String status;
    private Double average;
    private Integer students_with_garde;
    private Integer all_students;
    private Integer inactive_students; // inactive students last 7 days
    private List<String> featurse;
    private List<Double> scores;
    private Integer coursesNumber;
}
