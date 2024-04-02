package com.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;
import java.util.List;

import com.dashboard.dto.CoursesResponse;
import com.dashboard.services.CoursesService;

@RestController
@RequestMapping("/api/courses")
public class CoursesController {
    @Autowired
    private CoursesService coursesService;

    @GetMapping({ "/", "" })
    private CompletableFuture<ResponseEntity<List<CoursesResponse>>> getStudentsResponse() {
        return coursesService.getResponse().thenApply(
                response -> new ResponseEntity<>(response, HttpStatus.OK));
    }
}
