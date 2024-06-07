package com.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;
import java.util.List;

import com.dashboard.dto.CoursesResponse;
import com.dashboard.services.CoursesService;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://10.0.0.63:4300")
public class CoursesController {
    @Autowired
    private CoursesService coursesService;

    @GetMapping("/{i}")
    private CompletableFuture<ResponseEntity<List<CoursesResponse>>> getStudentsResponse(@PathVariable int i) {
        List<CoursesResponse> cachedResponse = coursesService.getCachedResponse(i);
        return CompletableFuture.completedFuture(new ResponseEntity<>(cachedResponse, HttpStatus.OK));
    }
}
