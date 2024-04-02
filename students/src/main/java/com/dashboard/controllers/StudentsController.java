package com.dashboard.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;

import com.dashboard.dto.StudentsResponse;
import com.dashboard.services.StudentsService;

@RestController
@RequestMapping("/api/students")
public class StudentsController {
    @Autowired
    private StudentsService studentsService;

    @GetMapping({ "/", "" })
    private CompletableFuture<ResponseEntity<List<StudentsResponse>>> getStudentsResponse() {
        return studentsService.getResponse().thenApply(
                response -> new ResponseEntity<>(response, HttpStatus.OK));
    }
}
