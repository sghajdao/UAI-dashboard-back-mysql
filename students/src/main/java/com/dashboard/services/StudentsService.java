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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dashboard.dto.StudentsResponse;
import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.entities.Canvas__submissions;
import com.dashboard.entities.Canvas__users;
import com.dashboard.repositories.EnrollmentsRepository;
import com.dashboard.repositories.ScoresRepository;
import com.dashboard.repositories.SubmissionsRepository;
import com.dashboard.repositories.UsersRepository;

@Service
public class StudentsService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EnrollmentsRepository enrollmentsRepository;
    @Autowired
    private ScoresRepository scoresRepository;
    @Autowired
    private SubmissionsRepository submissionsRepository;

    private List<StudentsResponse> cachedResponse;

    public List<Canvas__enrollments> getEnrolledStudents() {
        return enrollmentsRepository.getAllEnrollments(Date.from(Instant.parse("2024-01-01T00:00:00Z"))).stream()
                .filter(enrollment -> enrollment.getType().startsWith("StudentEnrollment"))
                .collect(Collectors.toList());
    }

    @Async
    public CompletableFuture<List<StudentsResponse>> getResponse() {
        List<Canvas__users> students = usersRepository.getAllStudents(Date.from(Instant.parse("2023-01-01T00:00:00Z")));
        List<Canvas__enrollments> enrollments = getEnrolledStudents();
        List<Canvas__scores> scores = scoresRepository
                .findScoresWithNonNullCurrentScore(Date.from(Instant.parse("2024-01-01T00:00:00Z")));
        List<Canvas__submissions> submissions = submissionsRepository
                .getAllSubmissions(Date.from(Instant.parse("2024-01-01T00:00:00Z")));

        Map<Long, List<Canvas__enrollments>> enrollmentsMap = enrollments.stream()
                .collect(Collectors.groupingBy(Canvas__enrollments::getUser_id));

        Map<Long, List<Canvas__submissions>> submissionsMap = submissions.stream()
                .collect(Collectors.groupingBy(Canvas__submissions::getUser_id));

        Map<Long, List<Canvas__scores>> scoresMap = scores.stream()
                .collect(Collectors.groupingBy(Canvas__scores::getEnrollment_id));

        List<StudentsResponse> response = students.parallelStream()
                .map(student -> {
                    List<Canvas__enrollments> studentEnrollments = enrollmentsMap.getOrDefault(student.getId(),
                            Collections.emptyList());
                    List<Double> coursesScores = new ArrayList<>();
                    double enrollmentAvg = 0;
                    long lastActivity = 0;
                    int attendedDays = 0;

                    for (Canvas__enrollments enrollment : studentEnrollments) {

                        List<Canvas__scores> studentScores = scoresMap.getOrDefault(enrollment.getId(),
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
                            coursesScores.isEmpty() ? 0
                                    : enrollmentAvg / coursesScores.size();

                    enrollmentsMap.remove(student.getId());

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

        response = updateSubmissions(response, submissionsMap);
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
