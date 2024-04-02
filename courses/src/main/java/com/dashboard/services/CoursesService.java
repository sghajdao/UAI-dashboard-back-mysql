package com.dashboard.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dashboard.dto.CoursesResponse;
import com.dashboard.entities.Canvas__courses;
import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.repositories.CoursesRepository;
import com.dashboard.repositories.EnrollmentsRepository;
import com.dashboard.repositories.ScoresRepository;

@Service
public class CoursesService {
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private EnrollmentsRepository enrollmentsRepository;
    @Autowired
    private ScoresRepository scoresRepository;

    @Async
    public CompletableFuture<List<CoursesResponse>> getResponse() {
        List<CoursesResponse> responses = new ArrayList<>();
        List<Canvas__courses> courses = coursesRepository.findAll();
        List<Canvas__enrollments> enrollments = enrollmentsRepository
                .getAllEnrollments(Date.from(Instant.parse("2024-01-01T00:00:00Z")));
        List<Canvas__scores> scores = scoresRepository
                .findScoresWithNonNullCurrentScore(Date.from(Instant.parse("2024-01-01T00:00:00Z")));

        Map<Long, List<Canvas__enrollments>> enrollmentsMap = enrollments.stream()
                .collect(Collectors.groupingBy(Canvas__enrollments::getCourse_id));

        Map<Long, List<Canvas__scores>> scoresMap = scores.stream()
                .collect(Collectors.groupingBy(Canvas__scores::getEnrollment_id));

        for (Canvas__courses course : courses) {
            CoursesResponse response = new CoursesResponse();
            response.setId(course.getId());
            response.setName(course.getName());
            if (course.getIs_public() != null && course.getIs_public() &&
                    course.getConclude_at() == null)
                response.setStatus("pulished");
            else if (course.getConclude_at() != null)
                response.setStatus("concluded");
            response.setFeaturse(new ArrayList<>(getFeatures(course)));

            List<Canvas__enrollments> courseEnrollments = enrollmentsMap.getOrDefault(course.getId(),
                    Collections.emptyList());
            double enrollmentAvg = 0;
            int studentsWithGrade = 0;
            int inactiveStudents = 0;
            for (Canvas__enrollments enrollment : courseEnrollments) {
                if (enrollment.getLast_activity_at() != null
                        && System.currentTimeMillis() - enrollment.getLast_activity_at().getTime() >= 86400000 * 7)
                    inactiveStudents++;
                List<Canvas__scores> courseScores = scoresMap.getOrDefault(enrollment.getId(),
                        Collections.emptyList());
                double current_scores = 0;
                int count = 0;
                for (Canvas__scores score : courseScores) {
                    if (score.getCurrent_score() != null) {
                        current_scores += score.getCurrent_score();
                        count++;
                    }
                }
                if (count != 0) {
                    studentsWithGrade++;
                    enrollmentAvg += current_scores / count;
                }
            }
            if (courseEnrollments.size() != 0)
                response.setAverage(enrollmentAvg / courseEnrollments.size());
            response.setStudents_with_garde(studentsWithGrade);
            response.setAll_students(courseEnrollments.size());
            response.setInactive_students(inactiveStudents);
            if (courseEnrollments.size() != 0)
                responses.add(response);
        }
        return CompletableFuture.completedFuture(responses);
    }

    List<String> getFeatures(Canvas__courses course) {
        List<String> features = new ArrayList<>();
        if (course.getGrading_standard_id() != null)
            features.add("grades");
        if (course.getSyllabus_body() != null)
            features.add("syllabus");
        if (course.getShow_announcements_on_home_page() != null)
            features.add("annoncements");
        if (course.getAllow_student_organized_groups())
            features.add("groups");
        if (course.getLatest_outcome_import_id() != null)
            features.add("outcomes");
        if (course.getSettings() != null) {
            String discussions = course.getSettings().split(",")[0];
            String files = course.getSettings().split(",")[course.getSettings().split(",").length - 1];
            if (discussions.contains("allow_student_discussion_editing") && discussions.contains("true"))
                features.add("discussion");
            if (files.contains("allow_student_forum_attachments") && files.contains("true")) {
                features.add("files");
            }
        }
        return features;
    }
}
