package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.strategy.DeliveryStrategy;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.voznja.Drive;
import org.foi.uzdiz.mmusica.voznja.GPS;

import java.time.LocalDateTime;

public class ActiveVehicleState implements VehicleState, Cloneable {
    private final DeliveryStrategy deliveryStrategy;

    public ActiveVehicleState(DeliveryStrategy deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }

    @Override
    public void finalizeDeliveries(Vehicle vehicle) {
        LocalDateTime currentTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        if (vehicle.isDriving() && (vehicle.getDeliveryFinishedBy().isEqual(currentTime)
                || vehicle.getDeliveryFinishedBy().isBefore(currentTime))) {

            deliveryStrategy.deliverPackages(vehicle);
            if (areAllPackagesDelivered(vehicle)) {
                deliveryStrategy.sendVehicleHome(vehicle);
            }
            //jedini problem je sto se gps zove currentGps i mijenja dok vozilo tehnicki jos ne dode do ureda
            if (areAllPackagesDelivered(vehicle) && vehicle.getCurrentGPS().equals(TerminalCommandHandler.getInstance().getOfficeGps())) {
                clearData(vehicle);
            }
        }
    }

    private boolean areAllPackagesDelivered(Vehicle vehicle) {
        return vehicle.getPackages().stream().allMatch(Paket::isDelivered);
    }

    @Override
    public Paket loadPackageIntoVehicle(Paket paket, Vehicle vehicle) {
        if (hasEnoughCapacity(vehicle, paket) && hasEnoughWeight(vehicle, paket)
                && !paket.isLoaded() && !paket.isErrored()) {

            vehicle.setCurrentlyLoadedCapacity(vehicle.getCurrentlyLoadedCapacity() + paket.calculatePackageSize());
            vehicle.setCurrentlyLoadedWeight(vehicle.getCurrentlyLoadedWeight() + paket.getTezina());

            paket.setLoaded(true);
            System.out.printf("VRIJEME %s: Ukrcan paket s oznakom %s hitnosti %s na vozilo %s%n",
                    TerminalCommandHandler.getInstance().getCroDateString(), paket.getOznaka(), paket.getUslugaDostave(),
                    vehicle.getOpis());
            paket.setStatusIsporuke("Ukrcan u vozilo");

            vehicle.getPackages().add(paket);
            return vehicle.getPackages().get(vehicle.getPackages().size() - 1);

        }
        return null;
    }

    @Override
    public void startDeliveringPackages(Vehicle vehicle) {
        vehicle.setDriving(true);
        GPS officeGPs = TerminalCommandHandler.getInstance().getOfficeGps();
        if (vehicle.getCurrentGPS().equals(officeGPs)) {
            vehicle.getDrives().add(new Drive());
        }
        deliveryStrategy.deliverPackages(vehicle);

        System.out.printf("Vozilo %s krenulo u isporuku i biti ce gotovo nakon %s%n", vehicle.getOpis(),
                vehicle.getCroatianDate(vehicle.getDeliveryFinishedBy()));
    }

    @Override
    public void clearData(Vehicle vehicle) {
        System.out.printf("VOZILO %s SE VRATILO U URED u virtualno vrijeme: %s%n",
                vehicle.getOpis(), TerminalCommandHandler.getInstance().getCroDateString());
        vehicle.getPackages().clear();
        vehicle.setCurrentlyLoadedWeight(0);
        vehicle.setCurrentlyLoadedCapacity(0);
        vehicle.setDriving(false);
        vehicle.setDeliveryFinishedBy(null);
    }

    @Override
    public String giveStatus() {
        return "Aktivan";
    }

    @Override
    public VehicleState cloneThis() {
        return this.clone();
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

    @Override
    public ActiveVehicleState clone() {
        try {
            ActiveVehicleState clone = (ActiveVehicleState) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
