package com.axity.dinosaurpark.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DinosaurTest {

    @Test
    void testCarnivoreProperties() {
        CarnivoreDinosaur carnivore = new CarnivoreDinosaur("Rexy", "Tyrannosaurus Rex");

        assertEquals("Rexy", carnivore.getName());
        assertEquals("Tyrannosaurus Rex", carnivore.getSpecies());
        assertEquals(500.0, carnivore.getFeedingCostPerDay());
        assertEquals("CARNIVORE", carnivore.getDiet());
        assertEquals(0.9, carnivore.getDangerLevel());
        assertEquals(DinosaurStatus.IN_ENCLOSURE, carnivore.getStatus());
    }

    @Test
    void testHerbivoreProperties() {
        HerbivoreDinosaur herbivore = new HerbivoreDinosaur("Littlefoot", "Apatosaurus");

        assertEquals("Littlefoot", herbivore.getName());
        assertEquals("Apatosaurus", herbivore.getSpecies());
        assertEquals(200.0, herbivore.getFeedingCostPerDay());
        assertEquals("HERBIVORE", herbivore.getDiet());
        assertEquals(0.2, herbivore.getDangerLevel());
        assertEquals(DinosaurStatus.IN_ENCLOSURE, herbivore.getStatus());
    }

    @Test
    void testStatusTransitions() {
        CarnivoreDinosaur dino = new CarnivoreDinosaur("Blue", "Velociraptor");
        assertEquals(DinosaurStatus.IN_ENCLOSURE, dino.getStatus());

        dino.escape();
        assertEquals(DinosaurStatus.ESCAPED, dino.getStatus());

        dino.recapture();
        assertEquals(DinosaurStatus.RECAPTURED, dino.getStatus());

        dino.returnToEnclosure();
        assertEquals(DinosaurStatus.IN_ENCLOSURE, dino.getStatus());

        dino.setStatus(DinosaurStatus.ESCAPED);
        assertEquals(DinosaurStatus.ESCAPED, dino.getStatus());
    }

    @Test
    void testIdAutoIncrement() {
        CarnivoreDinosaur d1 = new CarnivoreDinosaur("Dino1", "Species1");
        CarnivoreDinosaur d2 = new CarnivoreDinosaur("Dino2", "Species2");

        assertTrue(d1.getId() > 0);
        assertEquals(d1.getId() + 1, d2.getId());
    }
}
