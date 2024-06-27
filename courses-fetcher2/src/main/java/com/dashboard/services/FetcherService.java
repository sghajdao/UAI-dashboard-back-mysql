package com.dashboard.services;

import java.time.Instant;
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
import com.dashboard.entities.Canvas__assignments;
import com.dashboard.entities.Canvas__context_modules;
import com.dashboard.entities.Canvas__courses;
import com.dashboard.entities.Canvas__enrollments;
import com.dashboard.entities.Canvas__scores;
import com.dashboard.entities.Canvas__users;
import com.dashboard.entities.Canvas__web_conferences;
import com.dashboard.entities.Canvas__wiki_pages;
import com.dashboard.repositories.AssignmentsRepository;
import com.dashboard.repositories.Context_modulesRepository;
import com.dashboard.repositories.CoursesRepository;
import com.dashboard.repositories.EnrollmentsRepository;
import com.dashboard.repositories.ScoresRepository;
import com.dashboard.repositories.UsersRepository;
import com.dashboard.repositories.Web_conferencesRepository;
import com.dashboard.repositories.Wiki_pagesRepository;

@Service
public class FetcherService {
    private static final Date DATE1 = Date.from(Instant.parse("2023-03-01T00:00:00Z"));

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EnrollmentsRepository enrollmentsRepository;
    @Autowired
    private ScoresRepository scoresRepository;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private AssignmentsRepository assignmentsRepository;
    @Autowired
    private Context_modulesRepository contextModulesRepository;
    @Autowired
    private Web_conferencesRepository webConferencesRepository;
    @Autowired
    private Wiki_pagesRepository wikiPagesRepository;

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
        List<Canvas__courses> courses = coursesRepository.getAllCourses(date);
        List<Canvas__assignments> assignments = assignmentsRepository.getAllAssignments(date);
        List<Canvas__context_modules> modules = contextModulesRepository.getAllModules(date);
        List<Canvas__web_conferences> conferences = webConferencesRepository.getAllConfernces(date);
        List<Canvas__wiki_pages> pages = wikiPagesRepository.getAllPages(date);
        int count = coursesRepository.countCourses(date);

        return new DatabaseTables(students, courses, enrollments, scores, assignments, modules,
                conferences, pages, count);
    }

    @Async
    public CompletableFuture<CoursesData> getCoursesData() {
        DatabaseTables dbTables = getDatabaseTables();
        List<Canvas__courses> courses = dbTables.getCourses();
        List<Canvas__users> students = dbTables.getStudents();
        List<Canvas__enrollments> enrollments = dbTables.getEnrollments();
        List<Canvas__scores> scores = dbTables.getScores();
        List<Canvas__assignments> assignments = dbTables.getAssignments();
        List<Canvas__context_modules> modules = dbTables.getModules();
        List<Canvas__web_conferences> conferences = dbTables.getConferences();
        List<Canvas__wiki_pages> pages = dbTables.getPages();

        Map<Long, List<Canvas__enrollments>> enrollmentsMap = enrollments.stream()
                .collect(Collectors.groupingBy(Canvas__enrollments::getCourse_id));

        Map<Long, List<Canvas__scores>> scoresMap = scores.stream()
                .collect(Collectors.groupingBy(Canvas__scores::getEnrollment_id));

        int count = dbTables.getCoursesNumber();
        CoursesData data = new CoursesData(courses, students, enrollmentsMap, scoresMap, scores, assignments, modules,
                conferences, pages, count);
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
