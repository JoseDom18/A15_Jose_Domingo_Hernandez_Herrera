package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.DatabaseService;

import java.util.Random;

public class CentralHub implements ParkZone {
    private final ParkConfig config = ParkConfig.getInstance();
    private int currentOccupancy = 0;

    @Override
    public String getName() {
        return "Central Hub";
    }

    @Override
    public boolean hasCapacity() {
        return true;
    }

    @Override
    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    @Override
    public int getMaxCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void enter(Tourist tourist) {
        currentOccupancy++;
    }

    @Override
    public void exit(Tourist tourist) {
        currentOccupancy--;
    }

    public void visit(Tourist tourist, Random rng, DatabaseService writer) {
        enter(tourist);

        double souvenirPrice = config.getDouble("hub.souvenirPrice", 15.0);
        double purchaseProb = config.getDouble("hub.souvenirPurchaseProbability", 0.4);

        if (rng.nextDouble() < purchaseProb) {
            tourist.spend(souvenirPrice);
            writer.recordRevenue("SOUVENIR_SALE", souvenirPrice, tourist.getId(), getName());
        }

        exit(tourist);
    }
}