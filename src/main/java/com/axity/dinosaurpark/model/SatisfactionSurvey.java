package com.axity.dinosaurpark.model;

public record SatisfactionSurvey(int touristId, String enclosure, int score) {

    public SatisfactionSurvey {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }
    }
}
