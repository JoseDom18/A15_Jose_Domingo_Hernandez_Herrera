package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
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

class ObservationEnclosureTest {

    private CsvWriter mockCsvWriter;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        mockCsvWriter = Mockito.mock(CsvWriter.class);
        mockRandom = Mockito.mock(Random.class);
    }

    @Test
    void testBasicExperienceAttributes() {
        ObservationEnclosure enclosure = new ObservationEnclosure("T-Rex Enclosure", ExperienceType.BASIC);

        assertEquals("T-Rex Enclosure", enclosure.getName());
        assertEquals(20, enclosure.getMaxCapacity());
        assertTrue(enclosure.hasCapacity());
        assertEquals(0, enclosure.getCurrentOccupancy());

        enclosure.enter(new Tourist("Alice"));
        assertEquals(0, enclosure.getCurrentOccupancy());
    }

    @Test
    void testPremiumExperienceAttributes() {
        ObservationEnclosure enclosure = new ObservationEnclosure("Raptor Enclosure", ExperienceType.PREMIUM);
        assertEquals(12, enclosure.getMaxCapacity());
    }

    @Test
    void testVipExperienceAttributes() {
        ObservationEnclosure enclosure = new ObservationEnclosure("VIP Lounge", ExperienceType.VIP);
        assertEquals(5, enclosure.getMaxCapacity());
    }

    @Test
    void testEnterWithCapacityAndFee() {
        ObservationEnclosure enclosure = new ObservationEnclosure("T-Rex Enclosure", ExperienceType.BASIC);
        Tourist t = new Tourist("Alice");

        enclosure.enter(t, mockCsvWriter);

        assertEquals(1, enclosure.getCurrentOccupancy());
        assertEquals(10.0, t.getMoneySpent());

        verify(mockCsvWriter).recordRevenue(
                eq("ENCLOSURE_ENTRY_BASIC"),
                eq(10.0),
                eq(t.getId()),
                eq("T-Rex Enclosure")
        );
    }

    @Test
    void testEnterAtFullCapacity() {
        ObservationEnclosure enclosure = new ObservationEnclosure("VIP Lounge", ExperienceType.VIP);

        // Fill capacity
        for (int i = 0; i < 5; i++) {
            enclosure.enter(new Tourist("T" + i), mockCsvWriter);
        }

        assertEquals(5, enclosure.getCurrentOccupancy());
        assertFalse(enclosure.hasCapacity());

        Tourist t = new Tourist("Alice");
        enclosure.enter(t, mockCsvWriter);

        assertEquals(5, enclosure.getCurrentOccupancy());
        assertEquals(0.0, t.getMoneySpent());
    }

    @Test
    void testExit() {
        ObservationEnclosure enclosure = new ObservationEnclosure("T-Rex Enclosure", ExperienceType.BASIC);
        Tourist t = new Tourist("Alice");

        enclosure.enter(t, mockCsvWriter);
        assertEquals(1, enclosure.getCurrentOccupancy());

        enclosure.exit(t);
        assertEquals(0, enclosure.getCurrentOccupancy());
    }

    @Test
    void testConductSurvey() {
        ObservationEnclosure enclosure = new ObservationEnclosure("Premium Enclosure", ExperienceType.PREMIUM);
        Tourist t = new Tourist("Alice");

        when(mockRandom.nextInt(3)).thenReturn(1);

        SatisfactionSurvey survey = enclosure.conductSurvey(t, mockRandom);

        assertNotNull(survey);
        assertEquals(t.getId(), survey.touristId());
        assertEquals("Premium Enclosure", survey.enclosure());
        assertEquals(3, survey.score());
    }
}
