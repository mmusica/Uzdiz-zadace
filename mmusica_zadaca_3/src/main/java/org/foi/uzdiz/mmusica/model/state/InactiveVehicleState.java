package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;

public class InactiveVehicleState implements VehicleState, Cloneable {


    public InactiveVehicleState() {
    }

    @Override
    public void finalizeDeliveries(Vehicle vehicle) {
        System.out.println(vehicle.getRegistracija() + " " + vehicle.getOpis() + ": Neaktivno vozilo, ne finalizira dostave");
    }

    @Override
    public void startDeliveringPackages(Vehicle vehicle) {
        System.out.println(vehicle.getRegistracija() + " " + vehicle.getOpis() + ": Neaktivno vozilo, ne pocinje dostavljati");
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
        return "Neaktivan";
    }

    @Override
    public VehicleState cloneThis() {
        return this.clone();
    }

    @Override
    public InactiveVehicleState clone() {
        try {
            return (InactiveVehicleState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
