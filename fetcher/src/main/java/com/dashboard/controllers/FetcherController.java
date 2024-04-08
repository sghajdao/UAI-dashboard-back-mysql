package com.dashboard.controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dashboard.dto.CoursesData;
import com.dashboard.dto.StudentsData;
import com.dashboard.services.FetcherService;

@RestController
@RequestMapping("/api/fetcher")
public class FetcherController {
    @Autowired
    private FetcherService fetcherService;

    @GetMapping("/students")
    private CompletableFuture<ResponseEntity<StudentsData>> getStudentsData() {
        return fetcherService.getStudentsData().thenApply(
                response -> new ResponseEntity<>(response, HttpStatus.OK));
        // return new ResponseEntity<>(fetcherService.getStudentsData(), HttpStatus.OK);
    }

    @GetMapping("/courses")
    private CompletableFuture<ResponseEntity<CoursesData>> getCoursesData() {
        return fetcherService.getCoursesData().thenApply(
                response -> new ResponseEntity<>(response, HttpStatus.OK));
        // return new ResponseEntity<>(fetcherService.getStudentsData(), HttpStatus.OK);
    }
}
