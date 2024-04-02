package com.dashboard.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__courses;

@Repository
public interface CoursesRepository extends JpaRepository<Canvas__courses, Long> {
    @Query("SELECT c FROM Canvas__courses c WHERE c.conclude_at IS NOT NULL AND c.is_public IS NOT NULL")
    List<Canvas__courses> getAllCourses();
}
