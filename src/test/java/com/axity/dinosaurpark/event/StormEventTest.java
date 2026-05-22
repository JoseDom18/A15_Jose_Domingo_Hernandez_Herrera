package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class StormEventTest {

    private StormEvent event;
    private ParkState state;
    private Random rng;

    @BeforeEach
    void setUp() {
        event = new StormEvent();
        state = new ParkState();
        state.databaseService = Mockito.mock(DatabaseService.class);
        rng = new Random();
    }

    @Test
    void testMetadata() {
        assertEquals("STORM", event.getName());
        assertEquals("A heavy storm forced all tourists to seek shelter.", event.getDescription());
        assertEquals(0.04, event.getProbability(), 0.0001);
    }

    @Test
    void testToRecord() {
        long step = 10;
        EventRecord record = event.toRecord(step);
        assertNotNull(record);
        assertEquals(step, record.step());
        assertEquals("STORM", record.eventName());
        assertEquals("A heavy storm forced all tourists to seek shelter.", record.description());
        assertEquals("All Tourists", record.affectedEntities());
        assertNotNull(record.timestamp());
    }

    @Test
    void testExecute() {
        Tourist tourist1 = new Tourist("Alice");
        tourist1.setStatus(TouristStatus.IN_PARK);

        Tourist tourist2 = new Tourist("Bob");
        tourist2.setStatus(TouristStatus.WAITING);

        Tourist tourist3 = new Tourist("Charlie");
        tourist3.setStatus(TouristStatus.IN_PARK);

        state.tourists.add(tourist1);
        state.tourists.add(tourist2);
        state.tourists.add(tourist3);

        event.execute(state, rng);

        assertTrue(tourist1.getVisitedZones().contains("Storm Shelter"), "Alice debería haber buscado refugio.");
        assertTrue(tourist3.getVisitedZones().contains("Storm Shelter"), "Charlie debería haber buscado refugio.");

        assertFalse(tourist2.getVisitedZones().contains("Storm Shelter"), "Bob no estaba en el parque, no debería haber buscado refugio.");

        verify(state.databaseService).recordExpense(
                eq("STORM_CLEANUP"),
                eq(500.0),
                anyString()
        );
    }
}
