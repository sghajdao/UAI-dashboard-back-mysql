package com.dashboard.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__scores;

@Repository
public interface ScoresRepository extends JpaRepository<Canvas__scores, Long> {
    @Query("SELECT s FROM Canvas__scores s WHERE s.current_score IS NOT NULL AND YEAR(s.created_at) = YEAR(:startDate)")
    List<Canvas__scores> findScoresWithNonNullCurrentScore(@Param("startDate") Date startDate);
}
