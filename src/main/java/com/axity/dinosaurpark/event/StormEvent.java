package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class StormEvent implements SimulationEvent {

    @Override
    public String getName() {
        return "STORM";
    }

    @Override
    public String getDescription() {
        return "A heavy storm forced all tourists to seek shelter.";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        for (Tourist tourist : state.tourists) {
            if (tourist.getStatus() == TouristStatus.IN_PARK) {
                tourist.recordVisit("Storm Shelter");
            }
        }

        double cleaningCost = 500.0;
        state.csvWriter.recordExpense("STORM_CLEANUP", cleaningCost, "Cleaning and maintenance after storm");
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "All Tourists", LocalDateTime.now());
    }

    @Override
    public double getProbability() {
        return ParkConfig.getInstance().getDouble("event.storm.probability", 0.04);
    }
}