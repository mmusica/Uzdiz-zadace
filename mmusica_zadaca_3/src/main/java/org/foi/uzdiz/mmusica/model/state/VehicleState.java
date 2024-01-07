package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;

public interface VehicleState extends Cloneable {
    void finalizeDeliveries(Vehicle vehicle);

    void startDeliveringPackages(Vehicle vehicle);

    Paket loadPackageIntoVehicle(Paket paket, Vehicle vehicle);

    void clearData(Vehicle vehicle);

    String giveStatus();
    VehicleState cloneThis();
}
