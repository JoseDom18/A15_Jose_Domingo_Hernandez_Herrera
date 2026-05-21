package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.persistence.CsvWriter;

import java.util.Random;

public class PowerPlant {

    private final ParkConfig config = ParkConfig.getInstance();
    private boolean operational = true;
    private double totalEnergy = config.getDouble("powerplant.initialEnergy", 100.0);
    private final double consumption = config.getDouble("powerplant.consumptionPerStep", 1.5);


    public void triggerFailure(CvsWriter writer) {
        double failureProbability = config.getDouble("powerplant.failureProbability", 0.05);
        if (new  Random().nextDouble() < failureProbability) {
            this.operational = false;
            // TODO: activar acción de registrar en cvs la falla
        }
    }

    public void tick(Random rand, CvsWriter writer) {

        if (!isOperational()) return;

        this.totalEnergy -= this.consumption;

        if (this.totalEnergy <= 0) {
            this.totalEnergy = 0;
            this.operational = false;
        } else {
            triggerFailure(writer);
        }

    }

    public boolean isOperational() {
        return this.operational;
    }

    public void repair(CsvWriter writer) {
        if (!this.isOperational()) {

            if (this.totalEnergy <= 0) {
                // TODO: registrar mantenimiento
            } else {
                // TODO: registrar reparacion
            }

            this.totalEnergy = config.getDouble("powerplant.initialEnergy", 100.0);
            this.operational = true;
        }


    }
}
