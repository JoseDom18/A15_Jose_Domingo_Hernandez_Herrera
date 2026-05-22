package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.PowerPlant;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ParkMonitorTest {

    @Test
    void testPrintStatus() {
        ParkMonitor monitor = new ParkMonitor();
        ParkState state = new ParkState();

        Tourist t1 = new Tourist("Alice");
        t1.setStatus(TouristStatus.IN_PARK);
        Tourist t2 = new Tourist("Bob");
        t2.setStatus(TouristStatus.WAITING);
        state.tourists.add(t1);
        state.tourists.add(t2);

        CarnivoreDinosaur dino1 = new CarnivoreDinosaur("Rexy", "T-Rex");
        dino1.setStatus(DinosaurStatus.IN_ENCLOSURE);
        state.dinosaurs.add(dino1);

        Vehicle jeep1 = new Vehicle("J1");
        jeep1.setStatus(VehicleStatus.IN_USE);
        state.vehicles.add(jeep1);

        state.powerPlant = new PowerPlant(); 

        state.databaseService = Mockito.mock(DatabaseService.class);
        when(state.databaseService.getTotalRevenueAccumulated()).thenReturn(5000.0);
        when(state.databaseService.getTotalExpenseAccumulated()).thenReturn(1500.0);

        assertDoesNotThrow(() -> monitor.printStatus(10, state));

        state.databaseService = null;
        assertDoesNotThrow(() -> monitor.printStatus(20, state));

        state.powerPlant.forceBlackout();
        assertDoesNotThrow(() -> monitor.printStatus(30, state));
    }
}
