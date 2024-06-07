package com.dashboard.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dashboard.dto.CoursesData;
import com.dashboard.dto.CoursesResponse;
import com.dashboard.entities.Canvas__assignments;
import com.dashboard.entities.Canvas__context_modules;
import com.dashboard.entities.Canvas__courses;
import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.entities.Canvas__users;
import com.dashboard.entities.Canvas__web_conferences;
import com.dashboard.entities.Canvas__wiki_pages;

@Service
public class CoursesService {
    @Autowired
    private RestTemplate restTemplate;

    private List<CoursesResponse> cachedResponse;

    @Async
    public CompletableFuture<List<CoursesResponse>> getResponse() {
        CoursesData coursesData = restTemplate.getForObject("http://10.0.0.63:9292/api/fetcher/courses/3",
                CoursesData.class);
                
        List<CoursesResponse> responses = new ArrayList<>();

        for (Canvas__courses course : coursesData.getCourses()) {
            CoursesResponse response = new CoursesResponse();
            response.setCoursesNumber(coursesData.getCoursesNumber());
            response.setId(course.getId());
            response.setName(course.getName());
            response.setCreated_at(course.getCreated_at());
            if (course.getIs_public() != null && course.getIs_public() &&
                    course.getConclude_at() == null)
                response.setStatus("pulished");
            else if (course.getConclude_at() != null)
                response.setStatus("concluded");
            response.setFeaturse(new ArrayList<>(getFeatures(course, coursesData)));

            List<Canvas__enrollments> courseEnrollments = coursesData.getEnrollmentsMap().getOrDefault(course.getId(),
                    Collections.emptyList());
            double enrollmentAvg = 0;
            int studentsWithGrade = 0;
            int inactiveStudents = 0;
            List<Double> studentsScorses = new ArrayList<>();
            List<String> teachers = new ArrayList<>();
            for (Canvas__enrollments enrollment : courseEnrollments) {
                for (Canvas__users user : coursesData.getStudents()) {
                    if (user.getId().equals(enrollment.getUser_id())
                            && enrollment.getType().startsWith("TeacherEnrollment")) {
                        teachers.add(user.getName());
                        break;
                    }
                }
                if (enrollment.getLast_activity_at() != null
                        && System.currentTimeMillis() - enrollment.getLast_activity_at().getTime() >= 86400000 * 7)
                    inactiveStudents++;
                List<Canvas__scores> courseScores = coursesData.getScoresMap().getOrDefault(enrollment.getId(),
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
                    studentsScorses.add(current_scores / count);
                }
            }
            response.setTeachers(teachers);
            if (courseEnrollments.size() != 0 && studentsWithGrade != 0)
                response.setAverage( enrollmentAvg / studentsWithGrade);
            response.setStudents_with_garde(studentsWithGrade);
            response.setAll_students(courseEnrollments.size());
            response.setInactive_students(inactiveStudents);
            response.setScores(studentsScorses);
            if (courseEnrollments.size() != 0)
                responses.add(response);
        }
        return CompletableFuture.completedFuture(responses);
    }

    List<String> getFeatures(Canvas__courses course, CoursesData coursesData) {
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
            if (discussions.contains("allow_student_discussion_editing") && discussions.contains("true")) {
                features.add("discussion");
            }
            if (files.contains("allow_student_forum_attachments") && files.contains("true")) {
                features.add("files");
            }
        }
        for (Canvas__assignments assignments: coursesData.getAssignments()) {
            if (assignments.getContext_id().equals(course.getId())) {
                features.add("assignments");
                break;
            }
        }
        for (Canvas__context_modules module: coursesData.getModules()) {
            if (module.getContext_id().equals(course.getId())) {
                features.add("modules");
                break;
            }
        }
        for (Canvas__web_conferences conference: coursesData.getConferences()) {
            if (conference.getContext_id().equals(course.getId())) {
                features.add("conferences");
                break;
            }
        }
        for (Canvas__wiki_pages page: coursesData.getPages()) {
            if (page.getContext_id().equals(course.getId())) {
                features.add("pages");
                break;
            }
        }
        return features;
    }

    @Cacheable(value = "coursesResponseCache")
    public List<CoursesResponse> getCachedResponse() {
        return cachedResponse;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void refreshResponse() {
        System.out.println("START");
        cachedResponse = getResponse().join();
        System.out.println("END");
    }
}
