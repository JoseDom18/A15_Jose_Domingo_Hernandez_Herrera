package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BathroomZoneTest {

    private BathroomZone zone;
    private DatabaseService mockWriter;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        zone = new BathroomZone();
        mockWriter = Mockito.mock(DatabaseService.class);
        mockRandom = Mockito.mock(Random.class);
    }

    @Test
    void testBasicAttributes() {
        assertEquals("Bathroom Zone", zone.getName());
        assertEquals(10, zone.getMaxCapacity());
        assertTrue(zone.hasCapacity());
        assertEquals(0, zone.getCurrentOccupancy());
    }

    @Test
    void testEnterAndExit() {
        Tourist t = new Tourist("Alice");
        zone.enter(t);
        assertEquals(1, zone.getCurrentOccupancy());

        zone.exit(t);
        assertEquals(0, zone.getCurrentOccupancy());
    }

    @Test
    void testTryEnterWithSpaSale() {
        Tourist t = new Tourist("Alice");
        when(mockRandom.nextDouble()).thenReturn(0.1);

        zone.tryEnter(t, mockRandom, mockWriter);

        assertEquals(1, zone.getCurrentOccupancy());
        assertEquals(20.0, t.getMoneySpent());

        verify(mockWriter).recordRevenue(
                eq("SPA_SERVICE"),
                eq(20.0),
                eq(t.getId()),
                eq("Bathroom Zone")
        );
    }

    @Test
    void testTryEnterNoSpaSale() {
        Tourist t = new Tourist("Alice");
        when(mockRandom.nextDouble()).thenReturn(0.3);

        zone.tryEnter(t, mockRandom, mockWriter);

        assertEquals(1, zone.getCurrentOccupancy());
        assertEquals(0.0, t.getMoneySpent());

        verify(mockWriter, never()).recordRevenue(anyString(), anyDouble(), anyInt(), anyString());
    }

    @Test
    void testTryEnterAtFullCapacity() {
        for (int i = 0; i < 10; i++) {
            zone.enter(new Tourist("T" + i));
        }

        assertFalse(zone.hasCapacity());
        assertEquals(10, zone.getCurrentOccupancy());

        Tourist t = new Tourist("Alice");
        zone.tryEnter(t, mockRandom, mockWriter);

        assertEquals(10, zone.getCurrentOccupancy());
        verify(mockRandom, never()).nextDouble();
    }

    @Test
    void testTickDecrementsAndRemovesOccupants() {
        Tourist t = new Tourist("Alice");
        zone.enter(t); 

        zone.tick();
        assertEquals(1, zone.getCurrentOccupancy());
        assertFalse(t.getVisitedZones().contains("Bathroom Zone"));

        
        zone.tick();
        assertEquals(1, zone.getCurrentOccupancy());
        assertFalse(t.getVisitedZones().contains("Bathroom Zone"));

        zone.tick();
        assertEquals(0, zone.getCurrentOccupancy());
        assertTrue(t.getVisitedZones().contains("Bathroom Zone"));
    }
}
