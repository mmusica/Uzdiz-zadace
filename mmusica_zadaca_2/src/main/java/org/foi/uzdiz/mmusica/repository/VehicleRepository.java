package org.foi.uzdiz.mmusica.repository;

import org.foi.uzdiz.mmusica.model.Vehicle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VehicleRepository implements Repository<Vehicle>{
    private final List<Vehicle> vehiclesList = new ArrayList<>();

    public VehicleRepository() {
    }

    @Override
    public Vehicle save(Vehicle item) {
        vehiclesList.add(item);
        return vehiclesList.get(vehiclesList.size()-1);
    }

    @Override
    public void saveAll(List<Vehicle> listOfItems) {
        vehiclesList.addAll(listOfItems);
    }

    @Override
    public List<Vehicle> getAll() {
        return vehiclesList.stream().sorted(Comparator.comparing(Vehicle::getRedoslijed)).toList();
    }

    @Override
    public <J> Vehicle find(J id) {
        List<Vehicle> list = vehiclesList.stream().filter(vehicle -> vehicle.getRegistracija().equals(id)).toList();
        if(list.isEmpty() || (long) list.size() >1) return null;
        else return list.get(0);
    }
}
