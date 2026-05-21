package com.axity.dinosaurpark.model;

import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.zone.PowerPlant;

public class Technician extends Worker {

    public Technician(String name, double dailySalary) {
        super(name, dailySalary);
    }

    public void repairIfNeeded(PowerPlant plant, CsvWriter writer) {
        if (!plant.isOperational()) plant.repair(writer);
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }
}
