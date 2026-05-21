package com.axity.dinosaurpark.model;

public class CarnivoreDinosaur extends Dinosaur {

    public CarnivoreDinosaur(String name, String species) {
        super(name, species, 500.00);
    }

    @Override
    public String getDiet() {
        return "CARNIVORE";
    }

    @Override
    public double getDangerLevel() {
        return 0.9;
    }
}
