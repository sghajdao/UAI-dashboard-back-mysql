package com.dashboard.controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/students/{i}")
    private CompletableFuture<ResponseEntity<StudentsData>> getStudentsData(@PathVariable int i) {
        return fetcherService.getStudentsData(i).thenApply(
                response -> new ResponseEntity<>(response, HttpStatus.OK));
        // return new ResponseEntity<>(fetcherService.getStudentsData(), HttpStatus.OK);
    }

    @GetMapping("/courses/{i}")
    private CompletableFuture<ResponseEntity<CoursesData>> getCoursesData(@PathVariable int i) {
        return fetcherService.getCoursesData(i).thenApply(
                response -> new ResponseEntity<>(response, HttpStatus.OK));
        // return new ResponseEntity<>(fetcherService.getStudentsData(), HttpStatus.OK);
    }
}
