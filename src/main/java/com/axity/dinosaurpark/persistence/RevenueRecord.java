package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record RevenueRecord(
        long id,
        String type,
        double amount,
        int touristId,
        String zone,
        LocalDateTime timestamp) {

    @Override
    public String toString() {
        return id + "," + type + "," + amount + "," + touristId + "," + zone  + "," + timestamp;
    }
}
