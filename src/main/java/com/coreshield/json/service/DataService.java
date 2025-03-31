package com.coreshield.json.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coreshield.json.model.Location;
import com.coreshield.json.model.MetaData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Service
public class DataService {
    
    private List<Location> locations = new ArrayList<>();
    private List<MetaData> metadata = new ArrayList<>();

    @PostConstruct
    public void init() {
        loadJsonData();
    }

    private void loadJsonData() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            
            InputStream locationStream = getClass().getClassLoader().getResourceAsStream("location.json");
            if (locationStream == null) {
                throw new RuntimeException(" locations.json not found!");
            }
            locations = mapper.readValue(locationStream, new TypeReference<List<Location>>() {});

            // Read metadata.json
            InputStream metadataStream = getClass().getClassLoader().getResourceAsStream("metadata.json");
            if (metadataStream == null) {
                throw new RuntimeException(" metadata.json not found!");
            }
            metadata = mapper.readValue(metadataStream, new TypeReference<List<MetaData>>() {});

            
            
        } catch (Exception e) {
            throw new RuntimeException(" Error loading JSON files: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> getMergedData() {
        List<Map<String, Object>> mergedData = new ArrayList<>();

        if (locations == null || metadata == null) {
            // System.err.println(" locations or metadata is NULL!");
            return mergedData;
        }

        for (Location loc : locations) {
            for (MetaData meta : metadata) {
                if (loc.getId().equals(meta.getId())) { 
                    Map<String, Object> combined = new HashMap<>();
                    combined.put("id", loc.getId());
                    combined.put("latitude", loc.getLatitude());
                    combined.put("longitude", loc.getLongitude());
                    combined.put("type", meta.getType()); 
                    combined.put("rating", meta.getRating()); 
                    combined.put("reviews", meta.getReviews()); 
                    mergedData.add(combined);
                }
            }
        }
        return mergedData;
    }

    
    public Map<String, Long> countValidPoints() {
        return getMergedData().stream()
                .filter(loc -> (double) loc.get("rating") > 0) 
                .collect(Collectors.groupingBy(loc -> (String) loc.get("type"), Collectors.counting()));
    }

    
    public Map<String, Double> calculateAverageRatings() {
        return getMergedData().stream()
                .filter(loc -> (double) loc.get("rating") > 0) 
                .collect(Collectors.groupingBy(
                        loc -> (String) loc.get("type"), 
                        Collectors.averagingDouble(loc -> (double) loc.get("rating"))
                ));
    }


    public Map<String, Object> findMostReviewedLocation() {
        return getMergedData().stream()
                .max(Comparator.comparingInt(loc -> (int) loc.get("reviews")))
                .map(loc -> Map.of(
                        "id", loc.get("id"),
                        "type", loc.get("type"),
                        "reviews", loc.get("reviews")
                ))
                .orElse(Map.of("error", "No valid data"));
    }

    public List<Map<String, Object>> getIncompleteData() {
        List<Map<String, Object>> incompleteData = new ArrayList<>();
    
        for (Location loc : locations) {
            for (MetaData meta : metadata) {
                if (loc.getId().equals(meta.getId())) {
                    boolean isTypeMissing = Objects.isNull(meta.getType());
                    boolean isRatingMissing = meta.getRating() == null;  
                    boolean isReviewsMissing = meta.getReviews() == null; 
        
                    if (isTypeMissing || isRatingMissing || isReviewsMissing) {
                        Map<String, Object> incompleteEntry = new HashMap<>();
                        incompleteEntry.put("ID", loc.getId());
                        incompleteEntry.put("Latitude", loc.getLatitude());
                        incompleteEntry.put("Longitude", loc.getLongitude());
                        incompleteEntry.put("Type", isTypeMissing ? " Missing" : meta.getType());
                        
                     
                        incompleteEntry.put("Rating", isRatingMissing ? " Missing" : String.valueOf(meta.getRating()));
                        incompleteEntry.put("Reviews", isReviewsMissing ? " Missing" : String.valueOf(meta.getReviews()));
        
                        incompleteData.add(incompleteEntry);
                    }
                }
            }
        }
        return incompleteData;
    }
    
}
