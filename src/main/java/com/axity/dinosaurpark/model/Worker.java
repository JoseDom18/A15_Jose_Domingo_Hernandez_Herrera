package com.axity.dinosaurpark.model;

public abstract class Worker {
    private final int id;
    private static int idCounter = 1;
    private final String name;
    private final double dailySalary;

    public Worker(String name, double dailySalary) {
        this.id = idCounter++;
        this.name = name;
        this.dailySalary = dailySalary;
    }

    public abstract String getRole();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDailySalary() {
        return dailySalary;
    }
}
