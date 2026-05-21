package com.axity.dinosaurpark.model;

import java.util.ArrayList;
import java.util.List;

public class Tourist {
    /**
     * Identificador único
     */
    private final int id;
    /**
     * Varible para autoimcrento
     */
    private static int idCounter = 1;
    /**
     * Nombre del turista
     */
    private final String name;
    /**
     * Estado Actual
     */
    private TouristStatus status;
    /**
     * Acumula lo que gasta
     */
    private double moneySpent;
    /**
     * Historial de zonas visitadas
     */
    private List<String> visitedZones;

    public Tourist(String name) {
        this.id = idCounter++;
        this.name = name;
        this.status = TouristStatus.WAITING;
        this.moneySpent = 0;
        this.visitedZones = new ArrayList<>();

    }

    public void spend(double amount) {
        this.moneySpent += amount;
    }

    public void recordVisit(String zone) {
        this.visitedZones.add(zone);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TouristStatus getStatus() {
        return status;
    }

    public void setStatus(TouristStatus status) {
        this.status = status;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(double moneySpent) {
        this.moneySpent = moneySpent;
    }

    public List<String> getVisitedZones() {
        return visitedZones;
    }

    public void setVisitedZones(List<String> visitedZones) {
        this.visitedZones = visitedZones;
    }
}
