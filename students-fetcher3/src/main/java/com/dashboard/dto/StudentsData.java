package com.dashboard.dto;

import java.util.List;
import java.util.Map;

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
public class StudentsData {
    private List<Canvas__users> students;
    private Map<Long, List<Canvas__enrollments>> enrollmentsMap;
    private Map<Long, List<Canvas__submissions>> submissionsMap;
    private Map<Long, List<Canvas__scores>> scoresMap;
}
