package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class BathroomZone implements ParkZone{
    private final ParkConfig config = ParkConfig.getInstance();
    private final Map<Tourist, Integer> occupants = new HashMap<>();

    @Override
    public String getName() {
        return "Bathroom Zone";
    }

    @Override
    public boolean hasCapacity() {
        return getCurrentOccupancy() < getMaxCapacity();
    }

    @Override
    public int getCurrentOccupancy() {
        return occupants.size();
    }

    @Override
    public int getMaxCapacity() {
        return config.getInt("bathroom.maxCapacity", 10);
    }

    @Override
    public void enter(Tourist tourist) {
        if (hasCapacity()) {
            int duration = config.getInt("bathroom.useDurationSteps", 3);
            occupants.put(tourist, duration);
        }
    }

    public void tryEnter(Tourist tourist, Random rand, CsvWriter writer) {
        if (hasCapacity()) {
            enter(tourist);

            double spaPrice = config.getDouble("bathroom.spaPrice", 20.0);
            double purchaseProb = config.getDouble("bathroom.spaPurchaseProbability", 0.2);
            if (rand.nextDouble() < purchaseProb) {
                tourist.spend(spaPrice);
                // TODO: agregar CvsWriter
            }

        }
    }

    @Override
    public void exit(Tourist tourist) {
        occupants.remove(tourist);
    }

    public void tick() {
        Iterator<Map.Entry<Tourist, Integer>> iterator = occupants.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Tourist, Integer> entry = iterator.next();
            Tourist tourist = entry.getKey();
            int duration = entry.getValue() - 1;

            if (duration <= 0) {
                iterator.remove();
                tourist.recordVisit(this.getName());
            } else {
                entry.setValue(duration);
            }
        }
    }
}
