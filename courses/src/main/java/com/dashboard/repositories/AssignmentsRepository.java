package com.dashboard.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__assignments;

@Repository
public interface AssignmentsRepository extends JpaRepository<Canvas__assignments, Long> {
    @Query("SELECT COUNT(*) FROM Canvas__assignments e WHERE e.context_id = :id AND e.created_at >= :startDate")
    int countByContextId(@Param("id") Long id, @Param("startDate") Date startDate);
}
