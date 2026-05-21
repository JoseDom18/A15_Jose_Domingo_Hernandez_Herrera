package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class DinosaurEscapeEvent implements SimulationEvent {

    @Override
    public String getName() {
        return "DINOSAUR_ESCAPE";
    }

    @Override
    public String getDescription() {
        return "A dinosaur has escaped its enclosure!";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        List<Dinosaur> containedDinos = state.dinosaurs.stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
                .toList();

        if (containedDinos.isEmpty()) return;

        Dinosaur escapee = containedDinos.get(rng.nextInt(containedDinos.size()));
        escapee.escape();

        if (rng.nextDouble() < escapee.getDangerLevel()) {
            List<Tourist> activeTourists = state.tourists.stream()
                    .filter(t -> t.getStatus() == TouristStatus.IN_PARK)
                    .toList();

            if (!activeTourists.isEmpty()) {
                Tourist victim = activeTourists.get(rng.nextInt(activeTourists.size()));
                victim.setStatus(TouristStatus.ATTACKED);
                state.csvWriter.recordExpense("MEDICAL_EMERGENCY", 1500.0, "Medical compensation for tourist attack");
            }
        }
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Dinosaurs & Tourists", LocalDateTime.now());
    }

    @Override
    public double getProbability() {
        return ParkConfig.getInstance().getDouble("event.escape.probability", 0.05);
    }
}