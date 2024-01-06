package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;

public class BrokenVehicleState implements VehicleState{
    private final Vehicle vehicle;

    public BrokenVehicleState(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public void finalizeDeliveries() {
        System.out.println(this.vehicle.getRegistracija() + " " + vehicle.getOpis() +": Neispravno vozilo, ne finalizira dostave");

    }

    @Override
    public void startDeliveringPackages() {
        System.out.println(this.vehicle.getRegistracija() + " " + vehicle.getOpis() +": Neispravno vozilo, ne pocinje dostavljati");

    }

    @Override
    public Paket loadPackageIntoVehicle(Paket paket) {
        return null;
    }

    @Override
    public void clearData() {

    }

    @Override
    public String giveStatus() {
        return "Neispravan";
    }
}
