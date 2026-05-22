package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class BlackoutEvent implements SimulationEvent {

    @Override
    public String getName() {
        return "Blackout";
    }

    @Override
    public String getDescription() {
        return "It occurs a blackout event in the park.";
    }

    @Override
    public void execute(ParkState state, Random rng) {

        state.powerPlant.forceBlackout();
        double damageCost = 2000.0;
        state.databaseService.recordExpense("BLACKOUT_DAMAGE", damageCost, "Repairs for blackout in the park.");

    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, this.getName(), this.getDescription(), "PowerPlant", LocalDateTime.now());
    }

    @Override
    public double getProbability() {
       return ParkConfig.getInstance().getDouble("event.blackout.probability", 0.03);
    }
}
