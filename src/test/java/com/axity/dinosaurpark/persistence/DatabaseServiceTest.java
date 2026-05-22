package com.axity.dinosaurpark.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseServiceTest {

    private DatabaseService service;

    @BeforeEach
    void setUp() {
        service = new DatabaseService();
    }

    @AfterEach
    void tearDown() {
        if (service != null) {
            service.shutdown();
        }
    }

    @Test
    void testDatabaseOperations() throws Exception {
        assertEquals(0.0, service.getTotalRevenueAccumulated());
        assertEquals(0.0, service.getTotalExpenseAccumulated());

        service.recordRevenue("TICKET_SALE", 50.0, 99, "Arrival Zone");
        assertEquals(50.0, service.getTotalRevenueAccumulated());

        service.recordExpense("REPAIR", 350.0, "Power plant repairs");
        assertEquals(350.0, service.getTotalExpenseAccumulated());

        EventRecord event = new EventRecord(12L, "BLACKOUT", "Total blackout", "All", LocalDateTime.now());
        service.recordEvent(event);

        service.recordEvent(null);

        String dbUrl = "jdbc:h2:./output/parkdb;DB_CLOSE_DELAY=-1";
        try (Connection conn = DriverManager.getConnection(dbUrl, "sa", "")) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM revenues WHERE tourist_id = 99")) {
                assertTrue(rs.next());
                assertEquals("TICKET_SALE", rs.getString("type"));
                assertEquals(50.0, rs.getDouble("amount"));
                assertEquals("Arrival Zone", rs.getString("zone"));
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM expenses WHERE description = 'Power plant repairs'")) {
                assertTrue(rs.next());
                assertEquals(350.0, rs.getDouble("amount"));
                assertEquals("REPAIR", rs.getString("type"));
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM events WHERE description = 'Total blackout'")) {
                assertTrue(rs.next());
                assertEquals("BLACKOUT", rs.getString("event_name"));
                assertEquals(12L, rs.getLong("step"));
                assertEquals("All", rs.getString("affected_entities"));
            }
        }
    }
}
