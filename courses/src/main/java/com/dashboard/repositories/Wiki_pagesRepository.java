package com.dashboard.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__wiki_pages;

@Repository
public interface Wiki_pagesRepository extends JpaRepository<Canvas__wiki_pages, Long> {
    @Query("SELECT COUNT(*) FROM Canvas__wiki_pages e WHERE e.context_id = :id AND e.created_at >= :startDate")
    int countByContextId(@Param("id") Long id, @Param("startDate") Date startDate);
}
