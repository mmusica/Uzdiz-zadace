package org.foi.uzdiz.mmusica.repository;

import org.foi.uzdiz.mmusica.model.locations.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository implements Repository<Location>{
    List<Location> areas = new ArrayList<>();
    @Override
    public Location save(Location item) {
        areas.add(item);
        return areas.get(areas.size() - 1);
    }

    @Override
    public void saveAll(List<Location> listOfItems) {
        areas.addAll(listOfItems);
    }

    @Override
    public List<Location> getAll() {
        return areas;
    }

    @Override
    public <J> Location find(J id) {
        return null;
    }
}
