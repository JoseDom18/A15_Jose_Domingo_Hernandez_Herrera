package com.axity.dinosaurpark.simulation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationEngineTest {

    @Test
    void testSimulationEngineRun() {
        SimulationEngine engine = new SimulationEngine();
        assertNotNull(engine);

        assertDoesNotThrow(engine::run);
    }
}
