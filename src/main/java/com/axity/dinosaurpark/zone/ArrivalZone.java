package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;

public class ArrivalZone implements ParkZone{


    @Override
    public String getName() {
        return "Arrival Zone";
    }

    @Override
    public boolean hasCapacity() {
        return ;
    }

    @Override
    public int getCurrentOccupancy() {
        return 0;
    }

    @Override
    public int getMaxCapacity() {
        return 0;
    }

    @Override
    public void enter(Tourist tourist) {

    }

    @Override
    public void exit(Tourist tourist) {

    }
}
