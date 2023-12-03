package org.foi.uzdiz.mmusica.model.locations;

import java.util.ArrayList;
import java.util.List;

public class Area implements Location {

    Long id;
    List<Location> locationList = new ArrayList<>();
    @Override
    public void display() {

    }

    @Override
    public Long getId() {
        return id;
    }
}
