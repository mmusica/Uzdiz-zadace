package org.foi.uzdiz.mmusica.strategy;

import org.foi.uzdiz.mmusica.model.Vehicle;

public interface DeliveryStrategy {
    void deliverPackage(Vehicle vehicle);
    void sendVehicleHome(Vehicle vehicle);
}
