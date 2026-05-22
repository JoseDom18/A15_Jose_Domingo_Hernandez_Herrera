package com.axity.dinosaurpark.model;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.PowerPlant;

public class Technician extends Worker {

    public Technician(String name, double dailySalary) {
        super(name, dailySalary);
    }

    public void repairIfNeeded(ParkState state) {
        if (!state.powerPlant.isOperational()) {

            Vehicle jeep = state.vehicles.stream()
                            .filter(vehicle -> vehicle.getStatus() == VehicleStatus.AVAILABLE)
                                    .findFirst()
                                            .orElse(null);
            if (jeep != null) {
                jeep.setInUse(ParkConfig.getInstance().getInt("vehicles.busySteps", 3));
                state.powerPlant.repair(state.databaseService);
                return;
            } else {
                System.out.println("No jeep available");
            }
        }

        Vehicle brokenJeep = state.vehicles.stream()
                .filter(vehicle -> vehicle.getStatus() == VehicleStatus.BROKEN)
                .findFirst().orElse(null);

        if (brokenJeep != null) {
            brokenJeep.setStatus(VehicleStatus.AVAILABLE);
            state.databaseService.recordExpense("VEHICLE_MAINTENANCE", ParkConfig.getInstance().getDouble("vehicles.cost.repair", 300), "Repair Jeep: " + brokenJeep.getId());
        }
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }
}
