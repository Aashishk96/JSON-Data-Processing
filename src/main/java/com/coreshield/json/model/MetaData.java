package com.coreshield.json.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaData {
    private String id;
    private String type;
    private Double rating;
    private Integer reviews;

   
    public MetaData() {}

    
    @JsonProperty("id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @JsonProperty("type")
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @JsonProperty("rating")
    public Double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    @JsonProperty("reviews")
    public Integer getReviews() { return reviews; }
    public void setReviews(int reviews) { this.reviews = reviews; }
}
