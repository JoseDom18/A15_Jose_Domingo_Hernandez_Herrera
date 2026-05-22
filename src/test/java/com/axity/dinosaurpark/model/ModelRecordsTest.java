package com.axity.dinosaurpark.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ModelRecordsTest {

    @Test
    void testTicketRecord() {
        LocalDateTime now = LocalDateTime.now();
        Ticket ticket = new Ticket(100L, 200, 25.0, "GENERAL", now);

        assertEquals(100L, ticket.id());
        assertEquals(200, ticket.tourist());
        assertEquals(25.0, ticket.price());
        assertEquals("GENERAL", ticket.category());
        assertEquals(now, ticket.issuedAt());
    }

    @Test
    void testSatisfactionSurveyValid() {
        SatisfactionSurvey survey = new SatisfactionSurvey(1, "T-Rex Enclosure", 4);

        assertEquals(1, survey.touristId());
        assertEquals("T-Rex Enclosure", survey.enclosure());
        assertEquals(4, survey.score());
    }

    @Test
    void testSatisfactionSurveyInvalidLowScore() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SatisfactionSurvey(1, "T-Rex Enclosure", 0);
        });

        assertEquals("Score must be between 1 and 5", exception.getMessage());
    }

    @Test
    void testSatisfactionSurveyInvalidHighScore() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SatisfactionSurvey(1, "T-Rex Enclosure", 6);
        });

        assertEquals("Score must be between 1 and 5", exception.getMessage());
    }
}
