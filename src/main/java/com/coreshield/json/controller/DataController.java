package com.coreshield.json.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coreshield.json.service.DataService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")  
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/mergedData")
    public List<Map<String, Object>> getMergedData() {
        return dataService.getMergedData();
    }

    @GetMapping("/countValidPoints")
    public Map<String, Long> countValidPoints() {
        return dataService.countValidPoints();
    }

    @GetMapping("/averageRating")
    public Map<String, Double> calculateAverageRatings() {
        return dataService.calculateAverageRatings();
    }

    @GetMapping("/mostReviews")
    public Map<String, Object> findMostReviewedLocation() {
        return dataService.findMostReviewedLocation();
    }

    @GetMapping("/incompleteData")
    public ResponseEntity<List<Map<String, Object>>> getIncompleteData() {
        return ResponseEntity.ok(dataService.getIncompleteData());
    }

}
