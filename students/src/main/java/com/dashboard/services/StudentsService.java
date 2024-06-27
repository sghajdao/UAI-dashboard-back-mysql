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

import com.dashboard.dto.StudentsData;
import com.dashboard.dto.StudentsResponse;
import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.entities.Canvas__submissions;

@Service
public class StudentsService {
    @Autowired
    private RestTemplate restTemplate;

    private List<StudentsResponse> cachedResponse1;
    private List<StudentsResponse> cachedResponse2;
    private List<StudentsResponse> cachedResponse3;

    @Async
    public CompletableFuture<List<StudentsResponse>> getResponse(int i) {
        StudentsData studentsData;
        if (i == 1)
            studentsData = restTemplate.getForObject("http://10.0.0.63:8181/api/fetcher/students",
                    StudentsData.class);
        else if (i == 2)
            studentsData = restTemplate.getForObject("http://10.0.0.63:8282/api/fetcher/students",
                    StudentsData.class);
        else
            studentsData = restTemplate.getForObject("http://10.0.0.63:8383/api/fetcher/students",
                    StudentsData.class);

        List<StudentsResponse> response = studentsData.getStudents().parallelStream()
                .map(student -> {
                    List<Canvas__enrollments> studentEnrollments = studentsData.getEnrollmentsMap().getOrDefault(
                            student.getId(),
                            Collections.emptyList());
                    List<Double> coursesScores = new ArrayList<>();
                    double enrollmentAvg = 0;
                    long lastActivity = 0;
                    int attendedDays = 0;

                    for (Canvas__enrollments enrollment : studentEnrollments) {

                        List<Canvas__scores> studentScores = studentsData.getScoresMap().getOrDefault(
                                enrollment.getId(),
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
                            coursesScores.add(current_scores / count);
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
            long onTime = 0;
            long missing = 0;
            long late = 0;
            long excused = 0;

            for (Canvas__submissions sub : studentSubmissions) {
                if (sub.getLate_policy_status() != null) {
                    if (sub.getLate_policy_status().startsWith("missing")) {
                        missing++;
                    } else if (sub.getLate_policy_status().startsWith("late")) {
                        late++;
                    }
                }

                if (sub.getExcused() != null && sub.getExcused()) {
                    excused++;
                } else {
                    onTime++;
                }
            }

            student.setOn_time_submissions(onTime);
            student.setMissing_submissions(missing);
            student.setLate_submissions(late);
            student.setExecused_submissions(excused);
        });
        return response;
    }

    @Cacheable(value = "coursesResponseCache")
    public List<StudentsResponse> getCachedResponse(int i) {
        if (i == 1)
            return cachedResponse1;
        else if (i == 2)
            return cachedResponse2;
        else
            return cachedResponse3;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void refreshResponse1() {
        System.out.println("START1");
        cachedResponse1 = getResponse(1).join();
        System.out.println("END1");
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void refreshResponse2() {
        System.out.println("START2");
        cachedResponse2 = getResponse(2).join();
        System.out.println("END2");
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void refreshResponse3() {
        System.out.println("START3");
        cachedResponse3 = getResponse(3).join();
        System.out.println("END3");
    }
}
