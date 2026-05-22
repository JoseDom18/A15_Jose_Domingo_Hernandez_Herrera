package com.axity.dinosaurpark.persistence;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RecordEntitiesTest {

    @Test
    void testExpenseRecord() {
        LocalDateTime now = LocalDateTime.now();
        ExpenseRecord record = new ExpenseRecord(1L, "REPAIR", 250.0, "Repair jeep", now);

        assertEquals(1L, record.id());
        assertEquals("REPAIR", record.type());
        assertEquals(250.0, record.amount());
        assertEquals("Repair jeep", record.description());
        assertEquals(now, record.timestamp());

        String expectedCsv = "1,REPAIR,250.0,Repair jeep," + now;
        assertEquals(expectedCsv, record.toString());
    }

    @Test
    void testRevenueRecord() {
        LocalDateTime now = LocalDateTime.now();
        RevenueRecord record = new RevenueRecord(2L, "TICKET_SALE", 100.0, 42, "Arrival Zone", now);

        assertEquals(2L, record.id());
        assertEquals("TICKET_SALE", record.type());
        assertEquals(100.0, record.amount());
        assertEquals(42, record.touristId());
        assertEquals("Arrival Zone", record.zone());
        assertEquals(now, record.timestamp());

        String expectedCsv = "2,TICKET_SALE,100.0,42,Arrival Zone," + now;
        assertEquals(expectedCsv, record.toString());
    }

    @Test
    void testEventRecord() {
        LocalDateTime now = LocalDateTime.now();
        EventRecord record = new EventRecord(15L, "STORM", "Heavy rain storm", "Vehicles,Plants", now);

        assertEquals(15L, record.step());
        assertEquals("STORM", record.eventName());
        assertEquals("Heavy rain storm", record.description());
        assertEquals("Vehicles,Plants", record.affectedEntities());
        assertEquals(now, record.timestamp());

        String expectedCsv = "15,STORM,Heavy rain storm,Vehicles,Plants," + now;
        assertEquals(expectedCsv, record.toString());
    }
}
