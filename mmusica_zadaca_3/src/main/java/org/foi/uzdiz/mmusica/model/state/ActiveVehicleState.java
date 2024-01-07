package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.strategy.DeliveryStrategy;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.voznja.Drive;
import org.foi.uzdiz.mmusica.voznja.GPS;

import java.time.LocalDateTime;

public class ActiveVehicleState implements VehicleState {
    private final Vehicle vehicle;
    private final DeliveryStrategy deliveryStrategy;

    public ActiveVehicleState(Vehicle vehicle, DeliveryStrategy deliveryStrategy) {
        this.vehicle = vehicle;
        this.deliveryStrategy = deliveryStrategy;
    }

    @Override
    public void finalizeDeliveries() {
        LocalDateTime currentTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        if (this.vehicle.isDriving() && (this.vehicle.getDeliveryFinishedBy().isEqual(currentTime)
                || this.vehicle.getDeliveryFinishedBy().isBefore(currentTime))) {

            deliveryStrategy.deliverPackage(this.vehicle);
            if (areAllPackagesDelivered()) {
               deliveryStrategy.sendVehicleHome(this.vehicle);
            }
            //jedini problem je sto se gps zove currentGps i mijenja dok vozilo tehnicki jos ne dode do ureda
            if (areAllPackagesDelivered() && vehicle.getCurrentGPS().equals(TerminalCommandHandler.getInstance().getOfficeGps())) {
                this.clearData();
            }
        }
    }

    private boolean areAllPackagesDelivered() {
        return this.vehicle.getPackages().stream().allMatch(Paket::isDelivered);
    }

    @Override
    public Paket loadPackageIntoVehicle(Paket paket) {
        if (hasEnoughCapacity(vehicle, paket) && hasEnoughWeight(vehicle, paket)
                && !paket.isLoaded() && !paket.isErrored()) {

            this.vehicle.setCurrentlyLoadedCapacity(this.vehicle.getCurrentlyLoadedCapacity() + paket.calculatePackageSize());
            this.vehicle.setCurrentlyLoadedWeight(this.vehicle.getCurrentlyLoadedWeight() + paket.getTezina());

            paket.setLoaded(true);
            System.out.printf("VRIJEME %s: Ukrcan paket s oznakom %s hitnosti %s na vozilo %s%n",
                    TerminalCommandHandler.getInstance().getCroDateString(), paket.getOznaka(), paket.getUslugaDostave(),
                    vehicle.getOpis());
            paket.setStatusIsporuke("Ukrcan u vozilo");

            this.vehicle.getPackages().add(paket);
            return this.vehicle.getPackages().get(vehicle.getPackages().size() - 1);

        }
        return null;
    }

    @Override
    public void startDeliveringPackages() {
        this.vehicle.setDriving(true);
        GPS officeGPs = TerminalCommandHandler.getInstance().getOfficeGps();
        if (vehicle.getCurrentGPS().equals(officeGPs)) {
            this.vehicle.getDrives().add(new Drive());
        }
        deliveryStrategy.deliverPackage(this.vehicle);

        System.out.printf("Vozilo %s krenulo u isporuku i biti ce gotovo nakon %s%n", this.vehicle.getOpis(),
                vehicle.getCroatianDate(this.vehicle.getDeliveryFinishedBy()));
    }

    @Override
    public void clearData() {
        System.out.printf("VOZILO %s SE VRATILO U URED u virtualno vrijeme: %s%n",
                this.vehicle.getOpis(), TerminalCommandHandler.getInstance().getCroDateString());
        this.vehicle.getPackages().clear();
        this.vehicle.setCurrentlyLoadedWeight(0);
        this.vehicle.setCurrentlyLoadedCapacity(0);
        this.vehicle.setDriving(false);
        this.vehicle.setDeliveryFinishedBy(null);
    }

    @Override
    public String giveStatus() {
        return "Aktivan";
    }

    private boolean hasEnoughCapacity(Vehicle vehicle, Paket paket) {
        double capacity = vehicle.getKapacitetProstora() - vehicle.getCurrentlyLoadedCapacity();
        double packageSize = paket.calculatePackageSize();
        return packageSize <= capacity;
    }

    private boolean hasEnoughWeight(Vehicle vehicle, Paket paket) {
        double capacity = vehicle.getKapacitetTezine() - vehicle.getCurrentlyLoadedWeight();
        double packageWeight = paket.getTezina();
        return packageWeight <= capacity;
    }
}
