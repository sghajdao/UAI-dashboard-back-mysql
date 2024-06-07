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

        private DatabaseTables databaseTables1;
        private DatabaseTables databaseTables2;
        private DatabaseTables databaseTables3;

        public List<Canvas__enrollments> getEnrolledStudents(Date date) {
                return enrollmentsRepository.getAllEnrollments(date)
                                .stream()
                                .filter(enrollment -> enrollment.getType().startsWith("StudentEnrollment")
                                                || enrollment.getType().startsWith("TeacherEnrollment"))
                                .collect(Collectors.toList());
        }

        private DatabaseTables getDatabase(Date date) {
                List<Canvas__users> students = usersRepository.getAllStudents(date);
                List<Canvas__enrollments> enrollments = getEnrolledStudents(date);
                List<Canvas__scores> scores = scoresRepository
                                .findScoresWithNonNullCurrentScore(date);
        
                List<Canvas__submissions> submissions = submissionsRepository
                                .getAllSubmissions(date);
        
                List<Canvas__courses> courses = coursesRepository.getAllCourses(date);
                List<Canvas__assignments> assignments = new ArrayList<>(assignmentsRepository.getAllAssignments(date));
                List<Canvas__context_modules> modules = new ArrayList<>(context_modulesRepository.getAllModules(date));
                List<Canvas__web_conferences> conferences = new ArrayList<>(web_conferencesRepository.getAllConfernces(date));
                List<Canvas__wiki_pages> pages = new ArrayList<>(wiki_pagesRepository.getAllPages(date));
                int count = coursesRepository.countCourses(date);
                return new DatabaseTables(students, courses, enrollments, scores, submissions, assignments, modules, conferences, pages, count);
        }

        @Async
        public CompletableFuture<StudentsData> getStudentsData(int i) {
                List<Canvas__users> students = getDatabaseTables(i).getStudents();
                List<Canvas__enrollments> enrollments = getDatabaseTables(i).getEnrollments().stream()
                                .filter(std -> std.getType().startsWith("StudentEnrollment")).toList();
                List<Canvas__scores> scores = getDatabaseTables(i).getScores();
                List<Canvas__submissions> submissions = getDatabaseTables(i).getSubmissions();

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
        public CompletableFuture<CoursesData> getCoursesData(int i) {
                List<Canvas__courses> courses = getDatabaseTables(i).getCourses();
                List<Canvas__users> students = getDatabaseTables(i).getStudents();
                List<Canvas__enrollments> enrollments = getDatabaseTables(i).getEnrollments();
                List<Canvas__scores> scores = getDatabaseTables(i).getScores();
                List<Canvas__assignments> assignments = getDatabaseTables(i).getAssignments();
                List<Canvas__context_modules> modules = getDatabaseTables(i).getModules();
                List<Canvas__web_conferences> conferences = getDatabaseTables(i).getConferences();
                List<Canvas__wiki_pages> pages = getDatabaseTables(i).getPages();

                Map<Long, List<Canvas__enrollments>> enrollmentsMap = enrollments.stream()
                                .collect(Collectors.groupingBy(Canvas__enrollments::getCourse_id));

                Map<Long, List<Canvas__scores>> scoresMap = scores.stream()
                                .collect(Collectors.groupingBy(Canvas__scores::getEnrollment_id));

                int count = getDatabaseTables(i).getCoursesNumber();
                CoursesData data = new CoursesData(courses, students, enrollmentsMap, scoresMap, scores, assignments, modules, conferences, pages, count);
                return CompletableFuture.completedFuture(data);
        }

        @Cacheable(value = {"databaseTables1", "databaseTables2", "databaseTables3"})
        public DatabaseTables getDatabaseTables(int i) {
                if (i == 1)
                        return databaseTables1;
                else if (i == 2)
                        return databaseTables2;
                else
                        return databaseTables3;
        }

        @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
        public void refreshData1() {
                System.out.println("Start1");
                databaseTables1 = getDatabase(Date.from(Instant.parse("2022-03-01T00:00:00Z")));
                System.out.println("End1");
        }

        @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
        public void refreshData2() {
                System.out.println("Start2");
                databaseTables2 = getDatabase(Date.from(Instant.parse("2023-03-01T00:00:00Z")));
                System.out.println("End2");
        }

        @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
        public void refreshData3() {
                System.out.println("Start3");
                databaseTables3 = getDatabase(Date.from(Instant.parse("2024-03-01T00:00:00Z")));
                System.out.println("End3");
        }
}
