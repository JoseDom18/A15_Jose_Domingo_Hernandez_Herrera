package com.axity.dinosaurpark.model;

public class Vehicle {
    private final String id;
    private VehicleStatus status;
    private int busySteps;

    public Vehicle(String id) {
        this.id = id;
        this.status = VehicleStatus.AVAILABLE;
        this.busySteps = 0;
    }

    public String getId() {
        return id;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public void setInUse(int durationSteps) {
        this.status = VehicleStatus.IN_USE;
        this.busySteps = durationSteps;
    }

    public void tick() {
        if (this.status == VehicleStatus.IN_USE) {
            this.busySteps--;
            if (this.busySteps <= 0) {
                this.status = VehicleStatus.AVAILABLE;
            }
        }
    }

}