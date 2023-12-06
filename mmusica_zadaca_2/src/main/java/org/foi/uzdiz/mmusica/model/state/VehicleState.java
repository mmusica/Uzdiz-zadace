package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;

public interface VehicleState {
    void finalizeDeliveries();
    void startDeliveringPackages();
    Paket loadPackageIntoVehicle(Paket paket);
    void clearData();
}
