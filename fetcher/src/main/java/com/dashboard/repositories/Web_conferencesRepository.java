package com.dashboard.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__web_conferences;

@Repository
public interface Web_conferencesRepository extends JpaRepository<Canvas__web_conferences, Long> {
    // @Query("SELECT COUNT(*) FROM Canvas__web_conferences e WHERE e.context_id = :id AND e.created_at >= :startDate")
    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Canvas__web_conferences e WHERE e.context_id = :id) THEN true ELSE false END")
    boolean countByContextId(@Param("id") Long id);

    @Query("SELECT s FROM Canvas__web_conferences s WHERE s.created_at >= :startDate")
    List<Canvas__web_conferences> getAllConfernces(@Param("startDate") Date startDate);
}
