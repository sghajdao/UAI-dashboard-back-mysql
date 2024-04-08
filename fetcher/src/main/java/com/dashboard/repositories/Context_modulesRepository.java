package com.dashboard.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__context_modules;

@Repository
public interface Context_modulesRepository extends JpaRepository<Canvas__context_modules, Long> {
    // @Query("SELECT COUNT(*) FROM Canvas__context_modules e WHERE e.context_id = :id AND e.created_at >= :startDate")
    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Canvas__context_modules e WHERE e.context_id = :id) THEN true ELSE false END")
    boolean countByContextId(@Param("id") Long id);

    @Query("SELECT e FROM Canvas__context_modules e WHERE e.created_at >= :startDate")
    List<Canvas__context_modules> getAllModules(@Param("startDate") Date startDate);
}
