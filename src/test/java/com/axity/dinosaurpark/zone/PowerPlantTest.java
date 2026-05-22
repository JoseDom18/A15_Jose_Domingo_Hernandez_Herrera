package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PowerPlantTest {

    private PowerPlant powerPlant;
    private DatabaseService mockWriter;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        powerPlant = new PowerPlant();
        mockWriter = Mockito.mock(DatabaseService.class);
        mockRandom = Mockito.mock(Random.class);
    }

    @Test
    void testInitialStateIsOperational() {
        assertTrue(powerPlant.isOperational(), "La planta debería iniciar operativa.");
        assertEquals(100.0, powerPlant.getTotalEnergy(), "La energía inicial debe ser 100.");
    }

    @Test
    void testForceBlackoutShutsDownPlant() {
        powerPlant.forceBlackout();
        assertFalse(powerPlant.isOperational(), "La planta debe estar apagada tras un blackout.");
        assertEquals(0.0, powerPlant.getTotalEnergy(), "La energía debe ser 0 tras un blackout.");
    }

    @Test
    void testTickConsumesEnergy() {
        // Simulamos que el random devuelve 1.0 (nunca fallará aleatoriamente)
        Mockito.when(mockRandom.nextDouble()).thenReturn(1.0);

        powerPlant.tick(mockRandom, mockWriter);

        // 100.0 - 1.5 (consumo) = 98.5
        assertEquals(98.5, powerPlant.getTotalEnergy(), "El tick debe consumir 1.5 de energía.");
        assertTrue(powerPlant.isOperational());
    }

    @Test
    void testRepairAfterBlackoutLogsMaintenance() {
        powerPlant.forceBlackout();
        powerPlant.repair(mockWriter);

        assertTrue(powerPlant.isOperational(), "La planta debe volver a operar tras reparación.");
        assertEquals(100.0, powerPlant.getTotalEnergy(), "La energía debe restaurarse a 100.");

        // Verificamos que el mockWriter haya registrado el gasto de "MAINTENANCE"
        verify(mockWriter, times(1)).recordExpense(
                Mockito.eq("MAINTENANCE"),
                anyDouble(),
                anyString()
        );
    }
}