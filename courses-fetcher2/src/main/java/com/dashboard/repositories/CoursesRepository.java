package com.dashboard.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__courses;

@Repository
public interface CoursesRepository extends JpaRepository<Canvas__courses, Long> {
    // @Query("SELECT c FROM Canvas__courses c WHERE c.conclude_at IS NOT NULL AND c.is_public IS NOT NULL AND c.syllabus_body IS NOT NULL")
    // List<Canvas__courses> getAllCourses();

    @Query("SELECT a FROM Canvas__courses a WHERE YEAR(a.created_at) = YEAR(:startDate) AND a.syllabus_body IS NOT NULL")
    List<Canvas__courses> getAllCourses(@Param("startDate") Date startDate);

    @Query("SELECT COUNT(c) FROM Canvas__courses c WHERE YEAR(c.created_at) = YEAR(:startDate)")
    Integer countCourses(@Param("startDate") Date startDate);
}
