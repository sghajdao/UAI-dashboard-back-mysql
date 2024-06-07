package com.dashboard.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__submissions;

@Repository
public interface SubmissionsRepository extends JpaRepository<Canvas__submissions, Long> {

    @Query("SELECT a FROM Canvas__submissions a WHERE YEAR(a.created_at) = YEAR(:startDate)")
    List<Canvas__submissions> getAllSubmissions(@Param("startDate") Date startDate);
}
