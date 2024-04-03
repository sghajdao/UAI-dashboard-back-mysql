package com.dashboard.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__web_conferences;

@Repository
public interface Web_conferencesRepository extends JpaRepository<Canvas__web_conferences, Long> {
    @Query("SELECT COUNT(*) FROM Canvas__web_conferences e WHERE e.context_id = :id AND e.created_at >= :startDate")
    int countByContextId(@Param("id") Long id, @Param("startDate") Date startDate);
}
