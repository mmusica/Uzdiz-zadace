package org.foi.uzdiz.mmusica.model.locations;

import org.foi.uzdiz.mmusica.voznja.GPS;

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

    @Override
    public GPS getStartOfStreet(long id) {
        for (Location l : locationList) {
            GPS gps = l.getStartOfStreet(id);
            if (gps != null) return gps;
        }
        return null;
    }

    @Override
    public GPS getEndOfStreet(long id) {
        for (Location l : locationList) {
            GPS gps = l.getEndOfStreet(id);
            if (gps != null) return gps;
        }
        return null;
    }

    @Override
    public int getNajveciKbrUlice(long id) {
        for (Location l : locationList) {
            int nkbr = l.getNajveciKbrUlice(id);
            if (nkbr != -1) return nkbr;
        }
        return -1;
    }

}
