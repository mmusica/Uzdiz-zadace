package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;

public class BrokenVehicleState implements VehicleState, Cloneable {

    public BrokenVehicleState() {
    }

    @Override
    public void finalizeDeliveries(Vehicle vehicle) {
        System.out.println(vehicle.getRegistracija() + " " + vehicle.getOpis() + ": Neispravno vozilo, ne finalizira dostave");

    }

    @Override
    public void startDeliveringPackages(Vehicle vehicle) {
        System.out.println(vehicle.getRegistracija() + " " + vehicle.getOpis() + ": Neispravno vozilo, ne pocinje dostavljati");

    }

    @Override
    public Paket loadPackageIntoVehicle(Paket paket, Vehicle vehicle) {
        return null;
    }

    @Override
    public void clearData(Vehicle vehicle) {

    }

    @Override
    public String giveStatus() {
        return "Neispravan";
    }

    @Override
    public VehicleState cloneThis() {
        return this.clone();
    }

    @Override
    public BrokenVehicleState clone() {
        try {
            return (BrokenVehicleState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
