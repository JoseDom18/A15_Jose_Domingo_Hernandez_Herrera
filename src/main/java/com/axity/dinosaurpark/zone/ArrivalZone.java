package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Ticket;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.DatabaseService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrivalZone implements ParkZone{
    ParkConfig config = ParkConfig.getInstance();
    Queue<Tourist> waitingLine = new LinkedList<>();


    @Override
    public String getName() {
        return "Arrival Zone";
    }

    @Override
    public boolean hasCapacity() {
        return this.getMaxCapacity() > this.getCurrentOccupancy();
    }

    @Override
    public int getCurrentOccupancy() {
        return waitingLine.size();
    }

    @Override
    public int getMaxCapacity() {
        return config.getInt("arrival.maxCapacity", 30);
    }

    @Override
    public void enter(Tourist tourist) {
        waitingLine.offer(tourist);
    }

    @Override
    public void exit(Tourist tourist) {
    }

    public List<Ticket> processBatch(int batchSize, DatabaseService writer) {
        double ticketPrice = config.getDouble("arrival.ticketPrice", 25.0);
        int processed = 0;
        List<Ticket> generatedTickets = new ArrayList<>();

        while( processed < batchSize && !waitingLine.isEmpty() ) {
            Tourist tourist = waitingLine.poll();
            tourist.setStatus(TouristStatus.IN_PARK);
            tourist.spend(ticketPrice);
            writer.recordRevenue("TICKET_SALE", ticketPrice, tourist.getId(), getName());

            Ticket newTicket = new Ticket(
                    (long) tourist.getId(),
                    tourist.getId(),
                    ticketPrice,
                    "GENERAL",
                    LocalDateTime.now()
            );
            generatedTickets.add(newTicket);
            processed++;
        }

        return generatedTickets;
    }
}
