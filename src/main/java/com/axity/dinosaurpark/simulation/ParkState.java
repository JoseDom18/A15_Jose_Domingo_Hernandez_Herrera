package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.zone.ParkZone;
import com.axity.dinosaurpark.zone.PowerPlant;

import java.util.ArrayList;
import java.util.List;

public class ParkState {
    public final List<Tourist>  tourists = new ArrayList<>();
    public final List<Dinosaur> dinosaurs = new ArrayList<>();
    public final List<Worker> workers = new ArrayList<>();
    public final List<ParkZone> zones = new ArrayList<>();
    public PowerPlant powerPlant;
    public CsvWriter csvWriter;

    public double totalRevenue = 0.0;
    public double totalExpense = 0.0;

}
