package com.dashboard.services;

import java.time.Instant;
import java.util.ArrayList;
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

import com.dashboard.dto.CoursesData;
import com.dashboard.dto.DatabaseTables;
import com.dashboard.dto.StudentsData;
import com.dashboard.entities.Canvas__assignments;
import com.dashboard.entities.Canvas__context_modules;
import com.dashboard.entities.Canvas__courses;
import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.entities.Canvas__submissions;
import com.dashboard.entities.Canvas__users;
import com.dashboard.entities.Canvas__web_conferences;
import com.dashboard.entities.Canvas__wiki_pages;
import com.dashboard.repositories.AssignmentsRepository;
import com.dashboard.repositories.Context_modulesRepository;
import com.dashboard.repositories.CoursesRepository;
import com.dashboard.repositories.EnrollmentsRepository;
import com.dashboard.repositories.ScoresRepository;
import com.dashboard.repositories.SubmissionsRepository;
import com.dashboard.repositories.UsersRepository;
import com.dashboard.repositories.Web_conferencesRepository;
import com.dashboard.repositories.Wiki_pagesRepository;

@Service
public class FetcherService {
    @Autowired
        private UsersRepository usersRepository;
        @Autowired
        private EnrollmentsRepository enrollmentsRepository;
        @Autowired
        private ScoresRepository scoresRepository;
        @Autowired
        private SubmissionsRepository submissionsRepository;
        @Autowired
        private CoursesRepository coursesRepository;
        @Autowired
        private AssignmentsRepository assignmentsRepository;
        @Autowired
        private Context_modulesRepository context_modulesRepository;
        @Autowired
        private Web_conferencesRepository web_conferencesRepository;
        @Autowired
        private Wiki_pagesRepository wiki_pagesRepository;

        private DatabaseTables databaseTables;

        public List<Canvas__enrollments> getEnrolledStudents() {
                return enrollmentsRepository.getAllEnrollments(Date.from(Instant.parse("2023-03-01T00:00:00Z")))
                                .stream()
                                .filter(enrollment -> enrollment.getType().startsWith("StudentEnrollment")
                                                || enrollment.getType().startsWith("TeacherEnrollment"))
                                .collect(Collectors.toList());
        }

        private DatabaseTables getDatabase() {
                List<Canvas__users> students = usersRepository.getAllStudents(Date.from(Instant.parse("2023-03-01T00:00:00Z")));
                List<Canvas__enrollments> enrollments = getEnrolledStudents();
                List<Canvas__scores> scores = scoresRepository
                                .findScoresWithNonNullCurrentScore(Date.from(Instant.parse("2023-03-01T00:00:00Z")));
        
                List<Canvas__submissions> submissions = submissionsRepository
                                .getAllSubmissions(Date.from(Instant.parse("2023-03-01T00:00:00Z")));
        
                List<Canvas__courses> courses = coursesRepository.getAllCourses(Date.from(Instant.parse("2023-03-01T00:00:00Z")));
                List<Canvas__assignments> assignments = new ArrayList<>(assignmentsRepository.getAllAssignments(Date.from(Instant.parse("2023-03-01T00:00:00Z"))));
                List<Canvas__context_modules> modules = new ArrayList<>(context_modulesRepository.getAllModules(Date.from(Instant.parse("2023-03-01T00:00:00Z"))));
                List<Canvas__web_conferences> conferences = new ArrayList<>(web_conferencesRepository.getAllConfernces(Date.from(Instant.parse("2023-03-01T00:00:00Z"))));
                List<Canvas__wiki_pages> pages = new ArrayList<>(wiki_pagesRepository.getAllPages(Date.from(Instant.parse("2023-03-01T00:00:00Z"))));
                int count = coursesRepository.countCourses(Date.from(Instant.parse("2023-03-01T00:00:00Z")));
                return new DatabaseTables(students, courses, enrollments, scores, submissions, assignments, modules, conferences, pages, count);
        }

        @Async
        public CompletableFuture<StudentsData> getStudentsData() {
                List<Canvas__users> students = getDatabaseTables().getStudents();
                List<Canvas__enrollments> enrollments = getDatabaseTables().getEnrollments().stream()
                                .filter(std -> std.getType().startsWith("StudentEnrollment")).toList();
                List<Canvas__scores> scores = getDatabaseTables().getScores();
                List<Canvas__submissions> submissions = getDatabaseTables().getSubmissions();

                Map<Long, List<Canvas__enrollments>> enrollmentsMap = enrollments.stream()
                                .collect(Collectors.groupingBy(Canvas__enrollments::getUser_id));

                Map<Long, List<Canvas__submissions>> submissionsMap = submissions.stream()
                                .collect(Collectors.groupingBy(Canvas__submissions::getUser_id));

                Map<Long, List<Canvas__scores>> scoresMap = scores.stream()
                                .collect(Collectors.groupingBy(Canvas__scores::getEnrollment_id));

                StudentsData data = new StudentsData(students, enrollmentsMap, submissionsMap, scoresMap);
                return CompletableFuture.completedFuture(data);
        }

        @Async
        public CompletableFuture<CoursesData> getCoursesData() {
                List<Canvas__courses> courses = getDatabaseTables().getCourses();
                List<Canvas__users> students = getDatabaseTables().getStudents();
                List<Canvas__enrollments> enrollments = getDatabaseTables().getEnrollments();
                List<Canvas__scores> scores = getDatabaseTables().getScores();
                List<Canvas__assignments> assignments = getDatabaseTables().getAssignments();
                List<Canvas__context_modules> modules = getDatabaseTables().getModules();
                List<Canvas__web_conferences> conferences = getDatabaseTables().getConferences();
                List<Canvas__wiki_pages> pages = getDatabaseTables().getPages();

                Map<Long, List<Canvas__enrollments>> enrollmentsMap = enrollments.stream()
                                .collect(Collectors.groupingBy(Canvas__enrollments::getCourse_id));

                Map<Long, List<Canvas__scores>> scoresMap = scores.stream()
                                .collect(Collectors.groupingBy(Canvas__scores::getEnrollment_id));

                int count = getDatabaseTables().getCoursesNumber();
                CoursesData data = new CoursesData(courses, students, enrollmentsMap, scoresMap, scores, assignments, modules, conferences, pages, count);
                return CompletableFuture.completedFuture(data);
        }

        @Cacheable(value = "databaseTables")
        public DatabaseTables getDatabaseTables() {
                return databaseTables;
        }

        @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
        public void refreshData() {
                System.out.println("Start");
                databaseTables = getDatabase();
                System.out.println("End");
        }
}
