package org.foi.uzdiz.mmusica.model.locations;

import java.util.List;

public class Area implements Location {

    Long id;
    List<Location> locationList;

    public Area(Long id, List<Location> locationList) {
        this.id = id;
        this.locationList = locationList;
    }


    @Override
    public void display() {
        System.out.println("Podrucje "+this.id+".");
        this.locationList.forEach(Location::display);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Location findStreet(long id) {
        for (Location l : locationList) {
            Location location = l.findStreet(id);
            if (location != null) return location;
        }
        return null;
    }
}
