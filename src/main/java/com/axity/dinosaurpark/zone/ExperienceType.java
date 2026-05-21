package com.axity.dinosaurpark.zone;

public enum ExperienceType {
    BASIC(1, 3),
    PREMIUM(2, 4),
    VIP(3,5);

    private final int min;
    private final int max;

    ExperienceType(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }
    public int getMax() {
        return max;
    }

}
