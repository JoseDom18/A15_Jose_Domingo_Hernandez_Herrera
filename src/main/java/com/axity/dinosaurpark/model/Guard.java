package com.axity.dinosaurpark.model;

import java.util.List;

public class Guard extends Worker{

    public Guard(String name, double dailySalary) {
        super(name, dailySalary);
    }

    @Override
    public String getRole() {
        return "GUARD";
    }

    public void recapturedEscapedDinosaur(List<Dinosaur> dinosaurs) {
        dinosaurs.forEach(dinosaur -> {if (dinosaur.getStatus() == DinosaurStatus.ESCAPED) dinosaur.returnToEnclosure();});
    }
}
