package org.foi.uzdiz.mmusica.office;

import org.foi.uzdiz.mmusica.enums.TypeOfService;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DeliveryOffice {
    public final Deque<Vehicle> vehicles = new ArrayDeque<>();
    private LocalDateTime fullHourPassed = TerminalCommandHandler.getInstance().getVirtualniSat().plusHours(1);

    public DeliveryOffice() {
        Repository<Vehicle> vehicleRepository = RepositoryManager.getINSTANCE().getVehicleRepository();
        vehicles.addAll(vehicleRepository.getAll());
    }

    public List<Paket> loadPackages(List<Paket> receivedPackages) {
        boolean areAllVehiclesDriving = vehicles.stream().allMatch(Vehicle::isDriving);
        List<Paket> urgentPackages = getAllUrgentPackages(receivedPackages).stream().filter(paket -> !paket.isErrored()).toList();
        List<Paket> normalPackages = getAllNormalPackages(receivedPackages).stream().filter(paket -> !paket.isErrored()).toList();;
        while (!receivedPackages.isEmpty() && !areAllVehiclesDriving) {
            vehicles.forEach(vehicle -> {
                List<Paket> loadedPackages = getLoadedPackages(vehicle, urgentPackages, normalPackages);
                if (!loadedPackages.isEmpty()) {
                    receivedPackages.removeAll(loadedPackages);
                }
            });
            if (!receivedPackages.isEmpty()) break;
        }
        return receivedPackages;
    }

    private List<Paket> getLoadedPackages(Vehicle vehicle, List<Paket> urgentPackages, List<Paket> normalPackages) {
        List<Paket> loadedPackages = new ArrayList<>();
        Location foundLocation = findLocation(vehicle, urgentPackages);
        if (foundLocation == null) foundLocation = findLocation(vehicle, normalPackages);
        if (foundLocation != null) {
            for (Paket paket : urgentPackages) {
                if (paket.getDestination().equals(foundLocation)) {
                    Paket loadedPackage = vehicle.loadPackageIntoVehicle(paket);
                    if (loadedPackage != null) loadedPackages.add(loadedPackage);
                }
            }
            for (Paket paket : normalPackages) {
                if (paket.getDestination().equals(foundLocation)) {
                    Paket loadedPackage = vehicle.loadPackageIntoVehicle(paket);
                    if (loadedPackage != null) loadedPackages.add(loadedPackage);
                }
            }
        }
//        Paket loadedPackage = vehicle.loadPackageIntoVehicle(paket);
//        if (loadedPackage != null) loadedPackages.add(loadedPackag

        return loadedPackages;
    }

    private Location findLocation(Vehicle vehicle, List<Paket> packages) {
        Location foundLocation = null;
        for (Location location : vehicle.getDeliveryArea()) {
            for (Paket paket : packages) {
                if (paket.getDestination().equals(location)) {
                    foundLocation = location;
                    break;
                }
            }
            if (foundLocation != null) break;
        }
        return foundLocation;
    }

    private List<Paket> getAllNormalPackages(List<Paket> receivedPackages) {
        List<Paket> allNormalPackages = new ArrayList<>();
        receivedPackages.forEach(paket -> {
            if (!isUrgent(paket)) allNormalPackages.add(paket);
        });
        return allNormalPackages;
    }

    private List<Paket> getAllUrgentPackages(List<Paket> receivedPackages) {
        List<Paket> allUrgentPackages = new ArrayList<>();
        receivedPackages.forEach(paket -> {
            if (isUrgent(paket)) allUrgentPackages.add(paket);
        });
        return allUrgentPackages;
    }

    public void deliverPackages() {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isDriving()) vehicle.finalizeDeliveries();
            if(!vehicle.isDriving() && !vehicle.getPackages().isEmpty()) vehicle.startDeliveringPackages();
        }
        List<Vehicle> drivingVehicles = vehicles.stream().filter(Vehicle::isDriving).toList();
        vehicles.removeAll(drivingVehicles);
        vehicles.addAll(drivingVehicles);

        LocalDateTime currentHourTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        if (currentHourTime.isEqual(fullHourPassed) || fullHourPassed.isBefore(currentHourTime)) {
            fullHourPassed = fullHourPassed.plusHours(1);
        }
    }
    private boolean isUrgent(Paket paket) {
        return paket.getUslugaDostave().equals(TypeOfService.H.toString());
    }

}
