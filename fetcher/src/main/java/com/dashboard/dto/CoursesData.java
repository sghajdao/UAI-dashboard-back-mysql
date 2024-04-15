package com.dashboard.dto;

import java.util.List;
import java.util.Map;

import com.dashboard.entities.Canvas__assignments;
import com.dashboard.entities.Canvas__context_modules;
import com.dashboard.entities.Canvas__courses;
import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.entities.Canvas__web_conferences;
import com.dashboard.entities.Canvas__wiki_pages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursesData {
    private List<Canvas__courses> courses;
    private Map<Long, List<Canvas__enrollments>> enrollmentsMap;
    private Map<Long, List<Canvas__scores>> scoresMap;
    private List<Canvas__scores> scores;
    private List<Canvas__assignments> assignments;
    private List<Canvas__context_modules> modules;
    private List<Canvas__web_conferences> conferences;
    private List<Canvas__wiki_pages> pages;
    private Integer coursesNumber;
}
