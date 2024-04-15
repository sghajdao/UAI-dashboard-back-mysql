package com.dashboard.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__courses;

@Repository
public interface CoursesRepository extends JpaRepository<Canvas__courses, Long> {
    
}
