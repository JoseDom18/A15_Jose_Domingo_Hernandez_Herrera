package com.axity.dinosaurpark.model;

public class HerbivoreDinosaur extends Dinosaur {

    public HerbivoreDinosaur(String name, String species) {
        super(name, species, 200.0);
    }

    @Override
    public String getDiet() {
        return "HERBIVORE";
    }

    @Override
    public double getDangerLevel() {
        return 0.2;
    }
}
