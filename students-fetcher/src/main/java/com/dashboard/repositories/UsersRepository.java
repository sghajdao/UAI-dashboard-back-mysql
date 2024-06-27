package com.dashboard.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.entities.Canvas__users;

@Repository
public interface UsersRepository extends JpaRepository<Canvas__users, Long> {

    @Query("SELECT u FROM Canvas__users u WHERE YEAR(u.created_at) = YEAR(:startDate) AND u.name <> 'Test student' AND u.name <> 'Étudiant test' AND u.name <> 'Test Student'")
    List<Canvas__users> getAllStudents(@Param("startDate") Date startDate);
}
