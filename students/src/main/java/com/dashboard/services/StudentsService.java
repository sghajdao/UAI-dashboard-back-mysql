package com.dashboard.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dashboard.dto.Scores;
import com.dashboard.dto.StudentsData;
import com.dashboard.dto.StudentsResponse;
import com.dashboard.dto.Submissions;
import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.entities.Canvas__submissions;

@Service
public class StudentsService {
    @Autowired
    private RestTemplate restTemplate;

    private List<StudentsResponse> cachedResponse;

    @Async
    public CompletableFuture<List<StudentsResponse>> getResponse() {
        StudentsData studentsData = restTemplate.getForObject("http://10.0.0.63:9292/api/fetcher/students",
                StudentsData.class);

        List<StudentsResponse> response = studentsData.getStudents().parallelStream()
                .map(student -> {
                    List<Canvas__enrollments> studentEnrollments = studentsData.getEnrollmentsMap().getOrDefault(student.getId(),
                            Collections.emptyList());
                    List<Scores> coursesScores = new ArrayList<>();
                    double enrollmentAvg = 0;
                    long lastActivity = 0;
                    int attendedDays = 0;

                    for (Canvas__enrollments enrollment : studentEnrollments) {

                        List<Canvas__scores> studentScores = studentsData.getScoresMap().getOrDefault(enrollment.getId(),
                                Collections.emptyList());
                        double current_scores = 0;
                        int count = 0;
                        for (Canvas__scores score : studentScores) {
                            if (score.getCurrent_score() != null) {
                                current_scores += score.getCurrent_score();
                                count++;
                            }
                        }
                        if (count != 0) {
                            coursesScores.add(new Scores(current_scores / count, enrollment.getStart_at()));
                            enrollmentAvg += current_scores / count;
                        }

                        if (enrollment.getLast_activity_at() != null) {
                            long activityDifference = System.currentTimeMillis()
                                    - enrollment.getLast_activity_at().getTime();
                            lastActivity = Math.max(lastActivity, activityDifference);
                        }
                        if (enrollment.getLast_attended_at() != null) {
                            long attendedDifference = System.currentTimeMillis()
                                    - enrollment.getLast_attended_at().getTime();
                            attendedDays += attendedDifference;
                        }
                    }

                    int averageLastAttendedDays = studentEnrollments.isEmpty() ? 0
                            : attendedDays / studentEnrollments.size();
                    double averageEnrollmentAvg = studentEnrollments.isEmpty() ||
                            coursesScores.isEmpty() ? -1
                                    : enrollmentAvg / coursesScores.size();

                                    studentsData.getEnrollmentsMap().remove(student.getId());

                    if (studentEnrollments.size() == 0)
                        return null;
                    return StudentsResponse.builder()
                            .user_id(student.getId())
                            .user_sis_id(student.getId() != null ? student.getId() : -1)
                            .average_grade(averageEnrollmentAvg)
                            .courses_enrolled(studentEnrollments.size())
                            .courses_with_grade(coursesScores.size())
                            .name(student.getName())
                            .since_last_activity(lastActivity / 86400000)
                            .since_last_attended(averageLastAttendedDays / 86400000)
                            .courses_score(new ArrayList<>(coursesScores))
                            .build();
                })
                .filter(responses -> responses != null)
                .collect(Collectors.toList());

        response = updateSubmissions(response, studentsData.getSubmissionsMap());
        return CompletableFuture.completedFuture(response);
    }

    public List<StudentsResponse> updateSubmissions(List<StudentsResponse> response,
            Map<Long, List<Canvas__submissions>> submissionsMap) {
        response.forEach(student -> {
            List<Canvas__submissions> studentSubmissions = submissionsMap.getOrDefault(student.getUser_id(),
                    Collections.emptyList());
            // long onTime = 0;
            // long missing = 0;
            // long late = 0;
            // long excused = 0;
            List<Submissions> submissions = new ArrayList<>();

            for (Canvas__submissions sub : studentSubmissions) {
                if (sub.getLate_policy_status() != null) {
                    if (sub.getLate_policy_status().startsWith("missing")) {
                        submissions.add(new Submissions("missing", sub.getCreated_at()));
                    } else if (sub.getLate_policy_status().startsWith("late")) {
                        submissions.add(new Submissions("late", sub.getCreated_at()));
                    }
                }

                if (sub.getExcused() != null && sub.getExcused()) {
                    submissions.add(new Submissions("excused", sub.getCreated_at()));
                } else {
                    submissions.add(new Submissions("ontime", sub.getCreated_at()));
                }
            }

            student.setSubmissions(submissions);
            // student.setMissing_submissions(missing);
            // student.setLate_submissions(late);
            // student.setExecused_submissions(excused);
        });
        return response;
    }

    @Cacheable(value = "coursesResponseCache")
    public List<StudentsResponse> getCachedResponse() {
        System.out.println("REQUEST!!");
        return cachedResponse;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void refreshResponse() {
        System.out.println("START");
        cachedResponse = getResponse().join();
        System.out.println("END");
    }
}
