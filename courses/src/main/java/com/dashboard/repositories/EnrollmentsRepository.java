package com.dashboard.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__enrollments;

@Repository
public interface EnrollmentsRepository extends JpaRepository<Canvas__enrollments, Long> {
    @Query("SELECT e FROM Canvas__enrollments e WHERE e.created_at >= :startDate")
    List<Canvas__enrollments> getAllEnrollments(@Param("startDate") Date startDate);
}
