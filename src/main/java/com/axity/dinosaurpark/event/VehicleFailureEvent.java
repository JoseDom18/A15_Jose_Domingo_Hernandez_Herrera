package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.VehicleStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class VehicleFailureEvent implements SimulationEvent {

    @Override
    public String getName() {
        return "VEHICLE_FAILURE";
    }

    @Override
    public String getDescription() {
        return "Vehicle Failure Event";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        Vehicle target = state.vehicles.stream()
                .filter(v -> v.getStatus() == VehicleStatus.AVAILABLE)
                .findFirst().orElse(null);
        if (target != null) {
            target.setStatus(VehicleStatus.BROKEN);
        }

    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, this.getName(), this.getDescription(), "Vehicules", LocalDateTime.now());
    }

    @Override
    public double getProbability() {
        return ParkConfig.getInstance().getDouble("event.vehicle.failure", 0.05);
    }
}