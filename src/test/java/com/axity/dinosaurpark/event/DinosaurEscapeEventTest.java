package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DinosaurEscapeEventTest {

    private DinosaurEscapeEvent event;
    private ParkState state;
    private Random mockRng;

    @BeforeEach
    void setUp() {
        event = new DinosaurEscapeEvent();
        state = new ParkState();
        state.databaseService = Mockito.mock(DatabaseService.class);
        mockRng = Mockito.mock(Random.class);
    }

    @Test
    void testMetadata() {
        assertEquals("DINOSAUR_ESCAPE", event.getName());
        assertEquals("A dinosaur has escaped its enclosure!", event.getDescription());
        assertEquals(1.1, event.getProbability(), 0.0001);
    }

    @Test
    void testToRecord() {
        long step = 5;
        EventRecord record = event.toRecord(step);
        assertNotNull(record);
        assertEquals(step, record.step());
        assertEquals("DINOSAUR_ESCAPE", record.eventName());
        assertEquals("A dinosaur has escaped its enclosure!", record.description());
        assertEquals("Dinosaurs & Tourists", record.affectedEntities());
        assertNotNull(record.timestamp());
    }

    @Test
    void testExecuteWithNoDinos() {
        event.execute(state, mockRng);

        verify(mockRng, never()).nextInt(anyInt());
        verify(state.databaseService, never()).recordExpense(anyString(), eq(1500.0), anyString());
    }

    @Test
    void testExecuteWithDinosButNoAttack() {
        CarnivoreDinosaur dino1 = new CarnivoreDinosaur("Rexy", "Tyrannosaurus Rex");
        CarnivoreDinosaur dino2 = new CarnivoreDinosaur("Blue", "Velociraptor");
        dino2.setStatus(DinosaurStatus.ESCAPED); 
        state.dinosaurs.add(dino1);
        state.dinosaurs.add(dino2);

        Tourist tourist = new Tourist("Alice");
        tourist.setStatus(TouristStatus.IN_PARK);
        state.tourists.add(tourist);

        when(mockRng.nextInt(1)).thenReturn(0);
        when(mockRng.nextDouble()).thenReturn(0.95);

        event.execute(state, mockRng);

        assertEquals(DinosaurStatus.ESCAPED, dino1.getStatus());

        assertEquals(TouristStatus.IN_PARK, tourist.getStatus());
        
        verify(state.databaseService, never()).recordExpense(anyString(), eq(1500.0), anyString());
    }

    @Test
    void testExecuteWithDinosAndAttack() {
        CarnivoreDinosaur dino1 = new CarnivoreDinosaur("Rexy", "Tyrannosaurus Rex"); 

        Tourist tourist1 = new Tourist("Alice");
        tourist1.setStatus(TouristStatus.IN_PARK);
        Tourist tourist2 = new Tourist("Bob");
        tourist2.setStatus(TouristStatus.WAITING); 
        state.tourists.add(tourist1);
        state.tourists.add(tourist2);

        when(mockRng.nextInt(1)).thenReturn(0);
        when(mockRng.nextDouble()).thenReturn(0.8);

        event.execute(state, mockRng);

        assertEquals(DinosaurStatus.ESCAPED, dino1.getStatus());

        assertEquals(TouristStatus.ATTACKED, tourist1.getStatus());

        assertEquals(TouristStatus.WAITING, tourist2.getStatus());

        verify(state.databaseService).recordExpense(
                eq("MEDICAL_EMERGENCY"),
                eq(1500.0),
                anyString()
        );
    }
}
