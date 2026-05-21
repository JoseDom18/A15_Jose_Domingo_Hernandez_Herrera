package com.axity.dinosaurpark.model;

import java.time.LocalDateTime;

public record Ticket(Long id, int tourist, double price, String category, LocalDateTime issuedAt) {}