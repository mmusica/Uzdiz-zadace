package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.time.LocalDateTime;

public class ActiveVehicleState implements VehicleState {
    private final Vehicle vehicle;
    public ActiveVehicleState(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    @Override
    public void finalizeDeliveries() {
        LocalDateTime currentTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        if (this.vehicle.isDriving() && (this.vehicle.getDeliveryFinishedBy().isEqual(currentTime) || this.vehicle.getDeliveryFinishedBy().isBefore(currentTime))) {
            this.vehicle.getPackages().forEach(paket -> {
                paket.setBeingDelivered(false);
                paket.setDelivered(true);
                this.vehicle.setMoney(this.vehicle.getMoney().add(paket.getVehiclePrice()));
                paket.setStatusIsporuke("Dostavljen");

            });
            this.clearData();
            System.out.printf("ZAVRSENA DOSTAVA vozila %s u virtualno vrijeme: %s%n",
                    this.vehicle.getOpis(), TerminalCommandHandler.getInstance().getCroDateString());
        }
    }
    @Override
    public Paket loadPackageIntoVehicle(Paket paket) {
        this.vehicle.setGetCurrentlyLoadedCapacity(this.vehicle.getGetCurrentlyLoadedCapacity() + paket.calculatePackageSize());
        this.vehicle.setCurrentlyLoadedWeight(this.vehicle.getCurrentlyLoadedWeight() + paket.getTezina());
        paket.setBeingDelivered(true);
        paket.setStatusIsporuke("Ukrcan u vozilo");
        this.vehicle.getPackages().add(paket);
        return this.vehicle.getPackages().get(vehicle.getPackages().size()-1);
    }
    @Override
    public void startDeliveringPackages() {
        this.vehicle.setDriving(true);
        this.vehicle.setDeliveryFinishedBy(TerminalCommandHandler.getInstance().getVirtualniSat()
                .plusMinutes(TerminalCommandHandler.getInstance().getVrijemeIsporuke()));
        this.vehicle.getPackages().forEach(paket -> {
            paket.setBeingDelivered(true);
            paket.setStatusIsporuke("Trenutno u isporuci");
        });
        System.out.printf("Vozilo %s krenulo u isporuku i biti ce gotovo nakon %s%n", this.vehicle.getOpis(),
                vehicle.getCroatianDate(this.vehicle.getDeliveryFinishedBy()));
    }
    @Override
    public void clearData() {
        this.vehicle.getPackages().clear();
        this.vehicle.setCurrentlyLoadedWeight(0);
        this.vehicle.setGetCurrentlyLoadedCapacity(0);
        this.vehicle.setDriving(false);
        this.vehicle.setDeliveryFinishedBy(null);
    }
}
