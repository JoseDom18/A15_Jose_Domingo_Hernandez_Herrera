package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;

import java.util.Random;

public class ObservationEnclosure implements ParkZone {
    private final ParkConfig config = ParkConfig.getInstance();
    private final String name;
    private final ExperienceType type;
    private int currentOccupancy = 0;

    public ObservationEnclosure(String name, ExperienceType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return currentOccupancy < getMaxCapacity();
    }

    @Override
    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    @Override
    public int getMaxCapacity() {
        String configKey = "enclosure." + type.name().toLowerCase() + ".maxVisitors";
        return config.getInt(configKey, 20);
    }

    @Override
    public void enter(Tourist tourist) {

    }

    public void enter(Tourist tourist, CsvWriter writer) {
        if (hasCapacity()) {
            currentOccupancy++;

            String feeKey = "enclosure." + type.name().toLowerCase() + ".entryFee";
            double entryFee = config.getDouble(feeKey, 10.0);
            tourist.spend(entryFee);
            writer.recordRevenue("ENCLOSURE_ENTRY_" + type.name(), entryFee, tourist.getId(), getName());
        }
    }

    @Override
    public void exit(Tourist tourist) {
        currentOccupancy--;
    }

    public SatisfactionSurvey conductSurvey(Tourist tourist, Random rng) {
        int score = rng.nextInt((type.getMax() - type.getMin()) + 1 ) + type.getMin();
        return new SatisfactionSurvey(tourist.getId(), this.name, score);
    }
}