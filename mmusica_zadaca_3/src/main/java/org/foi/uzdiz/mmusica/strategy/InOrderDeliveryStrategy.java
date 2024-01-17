package org.foi.uzdiz.mmusica.strategy;

import org.foi.uzdiz.mmusica.model.Vehicle;

public class InOrderDeliveryStrategy implements DeliveryStrategy {

    private final PackageHandler packageHandler = new PackageHandler();

    @Override
    public void deliverPackages(Vehicle vehicle) {
        packageHandler.deliverPackage(vehicle);
    }

    @Override
    public void sendVehicleHome(Vehicle vehicle) {
        packageHandler.sendVehicleHome(vehicle);
    }
}
