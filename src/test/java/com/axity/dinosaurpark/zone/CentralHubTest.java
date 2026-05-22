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

class CentralHubTest {

    private CentralHub hub;
    private DatabaseService mockWriter;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        hub = new CentralHub();
        mockWriter = Mockito.mock(DatabaseService.class);
        mockRandom = Mockito.mock(Random.class);
    }

    @Test
    void testBasicAttributes() {
        assertEquals("Central Hub", hub.getName());
        assertEquals(Integer.MAX_VALUE, hub.getMaxCapacity());
        assertTrue(hub.hasCapacity());
        assertEquals(0, hub.getCurrentOccupancy());
    }

    @Test
    void testEnterAndExit() {
        Tourist t = new Tourist("Alice");
        hub.enter(t);
        assertEquals(1, hub.getCurrentOccupancy());

        hub.exit(t);
        assertEquals(0, hub.getCurrentOccupancy());
    }

    @Test
    void testVisitWithSouvenirPurchase() {
        Tourist t = new Tourist("Alice");
        when(mockRandom.nextDouble()).thenReturn(0.2);

        hub.visit(t, mockRandom, mockWriter);

        assertEquals(0, hub.getCurrentOccupancy());
        assertEquals(15.0, t.getMoneySpent());

        verify(mockWriter).recordRevenue(
                eq("SOUVENIR_SALE"),
                eq(15.0),
                eq(t.getId()),
                eq("Central Hub")
        );
    }

    @Test
    void testVisitWithoutSouvenirPurchase() {
        Tourist t = new Tourist("Alice");
        when(mockRandom.nextDouble()).thenReturn(0.6);

        hub.visit(t, mockRandom, mockWriter);

        assertEquals(0, hub.getCurrentOccupancy());
        assertEquals(0.0, t.getMoneySpent());

        verify(mockWriter, never()).recordRevenue(anyString(), anyDouble(), anyInt(), anyString());
    }
}
