package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.VehicleStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class VehicleFailureEventTest {

    private VehicleFailureEvent event;
    private ParkState state;
    private Random rng;

    @BeforeEach
    void setUp() {
        event = new VehicleFailureEvent();
        state = new ParkState();
        state.databaseService = Mockito.mock(DatabaseService.class);
        rng = new Random();
    }

    @Test
    void testMetadata() {
        assertEquals("VEHICLE_FAILURE", event.getName());
        assertEquals("Vehicle Failure Event", event.getDescription());
        assertEquals(0.05, event.getProbability(), 0.0001);
    }

    @Test
    void testToRecord() {
        long step = 20;
        EventRecord record = event.toRecord(step);
        assertNotNull(record);
        assertEquals(step, record.step());
        assertEquals("VEHICLE_FAILURE", record.eventName());
        assertEquals("Vehicle Failure Event", record.description());
        assertEquals("Vehicules", record.affectedEntities());
        assertNotNull(record.timestamp());
    }

    @Test
    void testExecuteWithAvailableVehicle() {
        Vehicle v1 = new Vehicle("V1");
        v1.setStatus(VehicleStatus.IN_USE);

        Vehicle v2 = new Vehicle("V2");
        v2.setStatus(VehicleStatus.AVAILABLE);

        Vehicle v3 = new Vehicle("V3");
        v3.setStatus(VehicleStatus.AVAILABLE);

        state.vehicles.add(v1);
        state.vehicles.add(v2);
        state.vehicles.add(v3);

        event.execute(state, rng);

        assertEquals(VehicleStatus.IN_USE, v1.getStatus());

        assertEquals(VehicleStatus.BROKEN, v2.getStatus());

        assertEquals(VehicleStatus.AVAILABLE, v3.getStatus());
    }

    @Test
    void testExecuteWithNoAvailableVehicle() {
        Vehicle v1 = new Vehicle("V1");
        v1.setStatus(VehicleStatus.IN_USE);

        Vehicle v2 = new Vehicle("V2");
        v2.setStatus(VehicleStatus.BROKEN);

        state.vehicles.add(v1);
        state.vehicles.add(v2);

        event.execute(state, rng);

        assertEquals(VehicleStatus.IN_USE, v1.getStatus());
        assertEquals(VehicleStatus.BROKEN, v2.getStatus());
    }
}
