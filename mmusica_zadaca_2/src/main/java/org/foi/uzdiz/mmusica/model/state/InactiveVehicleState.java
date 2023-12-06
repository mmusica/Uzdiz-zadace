package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;

public class InactiveVehicleState implements VehicleState{
    @Override
    public void finalizeDeliveries() {

    }

    @Override
    public void startDeliveringPackages() {

    }

    @Override
    public Paket loadPackageIntoVehicle(Paket paket) {
        return null;
    }

    @Override
    public void clearData() {

    }
}
