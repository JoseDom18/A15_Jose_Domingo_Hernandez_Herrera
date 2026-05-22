package com.axity.dinosaurpark.model;

import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.PowerPlant;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class WorkerTest {

    @Test
    void testGuardPropertiesAndRecapture() {
        Guard guard = new Guard("John", 200.0);

        assertEquals("John", guard.getName());
        assertEquals(200.0, guard.getDailySalary());
        assertEquals("GUARD", guard.getRole());
        assertTrue(guard.getId() > 0);

        List<Dinosaur> dinos = new ArrayList<>();
        CarnivoreDinosaur dino1 = new CarnivoreDinosaur("Rexy", "T-Rex");
        dino1.setStatus(DinosaurStatus.ESCAPED);

        CarnivoreDinosaur dino2 = new CarnivoreDinosaur("Blue", "Raptor");
        dino2.setStatus(DinosaurStatus.IN_ENCLOSURE);

        dinos.add(dino1);
        dinos.add(dino2);

        guard.recapturedEscapedDinosaur(dinos);

        assertEquals(DinosaurStatus.IN_ENCLOSURE, dino1.getStatus());
        assertEquals(DinosaurStatus.IN_ENCLOSURE, dino2.getStatus());
    }

    @Test
    void testTechnicianProperties() {
        Technician tech = new Technician("Bob", 250.0);
        assertEquals("Bob", tech.getName());
        assertEquals(250.0, tech.getDailySalary());
        assertEquals("TECHNICIAN", tech.getRole());
    }

    @Test
    void testTechnicianRepairsPowerPlantWithJeep() {
        Technician tech = new Technician("Bob", 250.0);
        ParkState state = new ParkState();
        state.databaseService = Mockito.mock(DatabaseService.class);
        state.powerPlant = new PowerPlant();
        state.powerPlant.forceBlackout(); 

        Vehicle jeep = new Vehicle("J1");
        jeep.setStatus(VehicleStatus.AVAILABLE);
        state.vehicles.add(jeep);

        tech.repairIfNeeded(state);

        assertTrue(state.powerPlant.isOperational());
        assertEquals(100.0, state.powerPlant.getTotalEnergy());

        assertEquals(VehicleStatus.IN_USE, jeep.getStatus());

        verify(state.databaseService).recordExpense(eq("MAINTENANCE"), eq(200.0), anyString());
    }

    @Test
    void testTechnicianPowerPlantNoJeep() {
        Technician tech = new Technician("Bob", 250.0);
        ParkState state = new ParkState();
        state.databaseService = Mockito.mock(DatabaseService.class);
        state.powerPlant = new PowerPlant();
        state.powerPlant.forceBlackout(); 

        Vehicle jeep = new Vehicle("J1");
        jeep.setStatus(VehicleStatus.IN_USE); 
        state.vehicles.add(jeep);

        tech.repairIfNeeded(state);
        assertFalse(state.powerPlant.isOperational());

        assertEquals(VehicleStatus.IN_USE, jeep.getStatus());

        verify(state.databaseService, never()).recordExpense(anyString(), anyDouble(), anyString());
    }

    @Test
    void testTechnicianRepairsBrokenJeep() {
        Technician tech = new Technician("Bob", 250.0);
        ParkState state = new ParkState();
        state.databaseService = Mockito.mock(DatabaseService.class);
        state.powerPlant = new PowerPlant(); 

        Vehicle brokenJeep = new Vehicle("J1");
        brokenJeep.setStatus(VehicleStatus.BROKEN);
        state.vehicles.add(brokenJeep);

        tech.repairIfNeeded(state);

        assertEquals(VehicleStatus.AVAILABLE, brokenJeep.getStatus());

        verify(state.databaseService).recordExpense(eq("VEHICLE_MAINTENANCE"), eq(300.0), anyString());
    }
}
