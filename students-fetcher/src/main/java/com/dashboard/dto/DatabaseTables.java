package com.dashboard.dto;

import java.util.List;

import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.entities.Canvas__submissions;
import com.dashboard.entities.Canvas__users;

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
    private List<Canvas__enrollments> enrollments;
    private List<Canvas__scores> scores;
    private List<Canvas__submissions> submissions;
}
