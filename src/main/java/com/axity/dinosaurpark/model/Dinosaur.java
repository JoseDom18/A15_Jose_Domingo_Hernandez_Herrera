package com.axity.dinosaurpark.model;

public abstract class Dinosaur {

    private final int id;
    private static int idCont = 1;
    private final String name;
    private final String species;
    private DinosaurStatus status;
    private final double feedingCostPerDay;

    public Dinosaur(String name, String species, double feedingCostPerDay) {
        id = idCont++;
        this.name = name;
        this.species = species;
        this.feedingCostPerDay = feedingCostPerDay;
        this.status = DinosaurStatus.IN_ENCLOSURE;
    }

    public abstract String getDiet();
    public abstract double getDangerLevel();

    public void escape() {
        this.status = DinosaurStatus.ESCAPED;
    }

    public void recapture() {
        this.status = DinosaurStatus.RECAPTURED;
    }

    public void returnToEnclosure() {
        this.status = DinosaurStatus.IN_ENCLOSURE;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public DinosaurStatus getStatus() {
        return status;
    }

    public void setStatus(DinosaurStatus status) {
        this.status = status;
    }

    public double getFeedingCostPerDay() {
        return feedingCostPerDay;
    }
}
