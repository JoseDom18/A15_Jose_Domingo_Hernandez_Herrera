package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Ticket;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ArrivalZoneTest {

    private ArrivalZone zone;
    private DatabaseService mockWriter;

    @BeforeEach
    void setUp() {
        zone = new ArrivalZone();
        mockWriter = Mockito.mock(DatabaseService.class);
    }

    @Test
    void testBasicAttributes() {
        assertEquals("Arrival Zone", zone.getName());
        assertEquals(30, zone.getMaxCapacity());
        assertTrue(zone.hasCapacity());
        assertEquals(0, zone.getCurrentOccupancy());

        Tourist t = new Tourist("Alice");
        zone.enter(t);
        assertEquals(1, zone.getCurrentOccupancy());

        zone.exit(t);
        assertEquals(1, zone.getCurrentOccupancy());
    }

    @Test
    void testProcessBatch() {
        Tourist t1 = new Tourist("Alice");
        Tourist t2 = new Tourist("Bob");
        Tourist t3 = new Tourist("Charlie");

        zone.enter(t1);
        zone.enter(t2);
        zone.enter(t3);

        assertEquals(3, zone.getCurrentOccupancy());

        List<Ticket> tickets = zone.processBatch(2, mockWriter);

        assertEquals(2, tickets.size());
        assertEquals(1, zone.getCurrentOccupancy());

        assertEquals(TouristStatus.IN_PARK, t1.getStatus());
        assertEquals(25.0, t1.getMoneySpent());

        assertEquals(TouristStatus.IN_PARK, t2.getStatus());
        assertEquals(25.0, t2.getMoneySpent());

        assertEquals(TouristStatus.WAITING, t3.getStatus());
        assertEquals(0.0, t3.getMoneySpent());

        verify(mockWriter, times(2)).recordRevenue(
                eq("TICKET_SALE"),
                eq(25.0),
                anyInt(),
                eq("Arrival Zone")
        );
    }
}
