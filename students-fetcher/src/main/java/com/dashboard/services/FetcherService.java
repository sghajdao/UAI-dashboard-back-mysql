package com.dashboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dashboard.dto.DatabaseTables;
import com.dashboard.dto.StudentsData;
import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.entities.Canvas__submissions;
import com.dashboard.entities.Canvas__users;
import com.dashboard.repositories.EnrollmentsRepository;
import com.dashboard.repositories.ScoresRepository;
import com.dashboard.repositories.SubmissionsRepository;
import com.dashboard.repositories.UsersRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class FetcherService {
    private static final Date DATE1 = Date.from(Instant.parse("2022-03-01T00:00:00Z"));

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EnrollmentsRepository enrollmentsRepository;
    @Autowired
    private ScoresRepository scoresRepository;
    @Autowired
    private SubmissionsRepository submissionsRepository;

    private DatabaseTables databaseTables;

    public List<Canvas__enrollments> getEnrolledStudents(Date date) {
        return enrollmentsRepository.getAllEnrollments(date).stream()
                .filter(enrollment -> enrollment.getType().startsWith("StudentEnrollment")
                        || enrollment.getType().startsWith("TeacherEnrollment"))
                .collect(Collectors.toList());
    }

    private DatabaseTables getDatabase(Date date) {
        List<Canvas__users> students = usersRepository.getAllStudents(date);
        List<Canvas__enrollments> enrollments = getEnrolledStudents(date);
        List<Canvas__scores> scores = scoresRepository.findScoresWithNonNullCurrentScore(date);
        List<Canvas__submissions> submissions = submissionsRepository.getAllSubmissions(date);

        return new DatabaseTables(students, enrollments, scores, submissions);
    }

    @Async
    public CompletableFuture<StudentsData> getStudentsData() {
        DatabaseTables dbTables = getDatabaseTables();
        List<Canvas__users> students = dbTables.getStudents();
        List<Canvas__enrollments> enrollments = dbTables.getEnrollments().stream()
                .filter(enrollment -> enrollment.getType().startsWith("StudentEnrollment"))
                .collect(Collectors.toList());
        List<Canvas__scores> scores = dbTables.getScores();
        List<Canvas__submissions> submissions = dbTables.getSubmissions();

        Map<Long, List<Canvas__enrollments>> enrollmentsMap = enrollments.stream()
                .collect(Collectors.groupingBy(Canvas__enrollments::getUser_id));

        Map<Long, List<Canvas__submissions>> submissionsMap = submissions.stream()
                .collect(Collectors.groupingBy(Canvas__submissions::getUser_id));

        Map<Long, List<Canvas__scores>> scoresMap = scores.stream()
                .collect(Collectors.groupingBy(Canvas__scores::getEnrollment_id));

        StudentsData data = new StudentsData(students, enrollmentsMap, submissionsMap, scoresMap);
        return CompletableFuture.completedFuture(data);
    }

    @Cacheable(value = "databaseTables")
    public DatabaseTables getDatabaseTables() {
        return databaseTables;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void refreshData1() {
        refreshData(DATE1);
    }

    private void refreshData(Date date) {
        System.out.println("Start");
        databaseTables = getDatabase(date);
        System.out.println("End");
    }
}
