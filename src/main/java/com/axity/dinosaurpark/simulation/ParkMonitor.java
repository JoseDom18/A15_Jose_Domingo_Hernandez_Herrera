package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.TouristStatus;

public class ParkMonitor {

    public void printStatus(long step, ParkState state) {
        long activeTourists = state.tourists.stream()
                .filter(t -> t.getStatus() == TouristStatus.IN_PARK)
                .count();

        long dinosInEnclosures = state.dinosaurs.stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
                .count();

        double currentEnergy = state.powerPlant.getTotalEnergy();
        boolean isOperational = state.powerPlant.isOperational();

        double revenue = state.csvWriter != null ? state.csvWriter.getTotalRevenueAccumulated() : 0.0;
        double expenses = state.csvWriter != null ? state.csvWriter.getTotalExpenseAccumulated() : 0.0;

        System.out.println("\n==================================================");
        System.out.println(" REPORTE DEL PARQUE | STEP: " + step);
        System.out.println("==================================================");
        System.out.println(" Turistas Activos: " + activeTourists + " / " + state.tourists.size());
        System.out.println(" Dinos en Recintos: " + dinosInEnclosures + " / " + state.dinosaurs.size());
        System.out.println(" Energía Planta: " + String.format("%.1f", currentEnergy) + "% [" + (isOperational ? "OPERATIVA" : "CAÍDA") + "]");
        System.out.println(" Vehículos en Uso: 0 (Fase Intermedia)");
        System.out.println(" Finanzas -> Ingresos: $" + revenue + " | Gastos: $" + expenses);
        System.out.println("==================================================\n");
    }
}