package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.PowerPlant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class BlackoutEventTest {

    private BlackoutEvent event;
    private ParkState state;
    private Random rng;

    @BeforeEach
    void setUp() {
        event = new BlackoutEvent();
        state = new ParkState();
        state.powerPlant = new PowerPlant();
        state.databaseService = Mockito.mock(DatabaseService.class);
        rng = new Random();
    }

    @Test
    void testMetadata() {
        assertEquals("Blackout", event.getName());
        assertEquals("It occurs a blackout event in the park.", event.getDescription());
        assertEquals(0.03, event.getProbability(), 0.0001);
    }

    @Test
    void testToRecord() {
        long step = 15;
        EventRecord record = event.toRecord(step);
        assertNotNull(record);
        assertEquals(step, record.step());
        assertEquals("Blackout", record.eventName());
        assertEquals("It occurs a blackout event in the park.", record.description());
        assertEquals("PowerPlant", record.affectedEntities());
        assertNotNull(record.timestamp());
    }

    @Test
    void testExecute() {
        assertTrue(state.powerPlant.isOperational());

        event.execute(state, rng);

        assertFalse(state.powerPlant.isOperational(), "La planta de energía debería quedar fuera de servicio.");
        assertEquals(0.0, state.powerPlant.getTotalEnergy(), 0.0001);

        verify(state.databaseService).recordExpense(
                eq("BLACKOUT_DAMAGE"),
                eq(2000.0),
                anyString()
        );
    }
}
