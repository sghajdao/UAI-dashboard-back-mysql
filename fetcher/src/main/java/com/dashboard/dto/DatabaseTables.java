package com.dashboard.dto;

import java.util.List;

import com.dashboard.entities.Canvas__assignments;
import com.dashboard.entities.Canvas__context_modules;
import com.dashboard.entities.Canvas__courses;
import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.entities.Canvas__submissions;
import com.dashboard.entities.Canvas__users;
import com.dashboard.entities.Canvas__web_conferences;
import com.dashboard.entities.Canvas__wiki_pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseTables {
    private List<Canvas__users> students;
    private List<Canvas__courses> courses;
    private List<Canvas__enrollments> enrollments;
    private List<Canvas__scores> scores;
    private List<Canvas__submissions> submissions;
    private List<Canvas__assignments> assignments;
    private List<Canvas__context_modules> modules;
    private List<Canvas__web_conferences> conferences;
    private List<Canvas__wiki_pages> pages;
}
