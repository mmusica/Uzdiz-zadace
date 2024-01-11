package org.foi.uzdiz.mmusica.strategy;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.voznja.Drive;
import org.foi.uzdiz.mmusica.voznja.GPS;
import org.foi.uzdiz.mmusica.voznja.Segment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
