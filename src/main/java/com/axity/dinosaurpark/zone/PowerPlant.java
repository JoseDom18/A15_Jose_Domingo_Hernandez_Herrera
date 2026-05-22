package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.persistence.CsvWriter;

import java.util.Random;

public class PowerPlant {

    private final ParkConfig config = ParkConfig.getInstance();
    private boolean operational = true;
    private double totalEnergy = config.getDouble("powerplant.initialEnergy", 100.0);
    private final double consumption = config.getDouble("powerplant.consumptionPerStep", 1.5);


    public void triggerFailure() {
        double failureProbability = config.getDouble("powerplant.failureProbability", 0.05);
        if (new  Random().nextDouble() < failureProbability) {
            this.operational = false;
        }
    }

    public void forceBlackout() {
        this.operational = false;
        this.totalEnergy = 0.0;
    }

    public void tick(Random rand, CsvWriter writer) {

        if (!isOperational()) return;

        this.totalEnergy -= this.consumption;

        if (this.totalEnergy <= 0) {
            this.totalEnergy = 0;
            this.operational = false;
        } else {
            triggerFailure();
        }

    }

    public boolean isOperational() {
        return this.operational;
    }

    public void repair(CsvWriter writer) {
        if (!this.isOperational()) {

            if (this.totalEnergy <= 0) {
                double cost = config.getDouble("powerplant.maintenanceCost", 200.0);
                writer.recordExpense("MAINTENANCE", cost, "Routine power plant maintenance");
            } else {
                double cost = config.getDouble("powerplant.repairCost", 500.0);
                writer.recordExpense("REPAIR", cost, "Unexpected power plant failure repair");
            }

            this.totalEnergy = config.getDouble("powerplant.initialEnergy", 100.0);
            this.operational = true;
        }
    }

    public double getTotalEnergy() {
        return this.totalEnergy;
    }
}
