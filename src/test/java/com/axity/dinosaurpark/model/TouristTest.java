package com.axity.dinosaurpark.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TouristTest {

    @Test
    void testTouristConstructorAndState() {
        Tourist tourist = new Tourist("John Hammond");

        assertTrue(tourist.getId() > 0);
        assertEquals("John Hammond", tourist.getName());
        assertEquals(TouristStatus.WAITING, tourist.getStatus());
        assertEquals(0.0, tourist.getMoneySpent());
        assertNotNull(tourist.getVisitedZones());
        assertTrue(tourist.getVisitedZones().isEmpty());
    }

    @Test
    void testSpendAndVisitRecording() {
        Tourist tourist = new Tourist("Ellie Sattler");

        tourist.spend(150.0);
        assertEquals(150.0, tourist.getMoneySpent());

        tourist.spend(50.0);
        assertEquals(200.0, tourist.getMoneySpent());

        tourist.recordVisit("Arrival Zone");
        tourist.recordVisit("Bathroom Zone");

        List<String> visited = tourist.getVisitedZones();
        assertEquals(2, visited.size());
        assertEquals("Arrival Zone", visited.get(0));
        assertEquals("Bathroom Zone", visited.get(1));
    }

    @Test
    void testSetters() {
        Tourist tourist = new Tourist("Alan Grant");

        tourist.setStatus(TouristStatus.IN_PARK);
        assertEquals(TouristStatus.IN_PARK, tourist.getStatus());

        tourist.setMoneySpent(500.0);
        assertEquals(500.0, tourist.getMoneySpent());

        List<String> newZones = new ArrayList<>();
        newZones.add("Central Hub");
        tourist.setVisitedZones(newZones);
        assertEquals(1, tourist.getVisitedZones().size());
        assertEquals("Central Hub", tourist.getVisitedZones().get(0));
    }
}
