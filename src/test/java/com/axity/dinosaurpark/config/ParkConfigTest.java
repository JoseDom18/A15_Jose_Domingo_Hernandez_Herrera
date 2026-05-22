package com.axity.dinosaurpark.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkConfigTest {

    @BeforeEach
    void setUp() {
        ParkConfig.resetForTesting();
    }

    @Test
    void testGetInstance() {
        ParkConfig first = ParkConfig.getInstance();
        ParkConfig second = ParkConfig.getInstance();
        assertNotNull(first);
        assertSame(first, second, "getInstance() should always return the same singleton instance.");
    }

    @Test
    void testGetInt() {
        ParkConfig config = ParkConfig.getInstance();

        assertEquals(42, config.getInt("simulation.seed", 999));
        assertEquals(100, config.getInt("simulation.totalSteps", 999));

        assertEquals(999, config.getInt("non.existent.key", 999));
    }

    @Test
    void testGetDouble() {
        ParkConfig config = ParkConfig.getInstance();

        assertEquals(42.0, config.getDouble("simulation.seed", 999.0));
        assertEquals(1.5, config.getDouble("powerplant.consumptionPerStep", 999.0));

        assertEquals(999.0, config.getDouble("non.existent.key", 999.0));
    }

    @Test
    void testGetString() {
        ParkConfig config = ParkConfig.getInstance();

        assertEquals("output", config.getString("output.directory", "defaultDir"));

        assertEquals("defaultDir", config.getString("non.existent.key", "defaultDir"));
    }

    @Test
    void testGetSeedAndTotalSteps() {
        ParkConfig config = ParkConfig.getInstance();

        assertEquals(42, config.getSeed());
        assertEquals(100, config.getTotalSteps());
    }
}
