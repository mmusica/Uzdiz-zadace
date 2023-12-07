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
        if (foundLocation == null) foundLocation = findLocation(vehicle, loadedPackages);
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
//        if (loadedPackage != null) loadedPackages.add(loadedPackage);

        return loadedPackages;
    }

    private Location findLocation(Vehicle vehicle, List<Paket> urgentPackages) {
        Location foundLocation = null;
        for (Location location : vehicle.getDeliveryArea()) {
            for (Paket paket : urgentPackages) {
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
        //Nakon što se popuni kapacitet vozila
        // ili nakon punog sata vozilo koje ima barem jedan ukrcani paket hitne dostave
        // ili je popunjeno minimalno 50% kapaciteta (težine ili prostora) kreće prema odredištima paketa.
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isDriving()) vehicle.finalizeDeliveries();
            if(isHourLater() && !vehicle.isDriving() && !vehicle.getPackages().isEmpty()) vehicle.startDeliveringPackages();
//            if ((isVehicleCapacityFilled(vehicle) || isHourLaterWithUrgentPackage(vehicle)
//                    || is50percentFull(vehicle)) && !vehicle.isDriving() && !vehicle.getPackages().isEmpty()) {
//
//            }
        }
        List<Vehicle> drivingVehicles = vehicles.stream().filter(Vehicle::isDriving).toList();
        vehicles.removeAll(drivingVehicles);
        vehicles.addAll(drivingVehicles);

        LocalDateTime currentHourTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        if (currentHourTime.isEqual(fullHourPassed) || fullHourPassed.isBefore(currentHourTime)) {
            fullHourPassed = fullHourPassed.plusHours(1);
        }
    }

    private boolean is50percentFull(Vehicle vehicle) {
        double halfWeight = vehicle.getKapacitetTezine() / 2;
        double halfCapacity = vehicle.getKapacitetProstora() / 2;
        return vehicle.getGetCurrentlyLoadedCapacity() >= halfCapacity || vehicle.getCurrentlyLoadedWeight() >= halfWeight;
    }

    private boolean isHourLaterWithUrgentPackage(Vehicle vehicle) {
        LocalDateTime currentHourTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        List<Paket> list = vehicle.getPackages().stream().filter(paket -> paket.getUslugaDostave().equals(TypeOfService.H.toString())).toList();
        if (currentHourTime.isEqual(fullHourPassed) || fullHourPassed.isBefore(currentHourTime)) {
            return !list.isEmpty();
        }
        return false;
    }
    private boolean isHourLater() {
        LocalDateTime currentHourTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        return currentHourTime.isEqual(fullHourPassed) || fullHourPassed.isBefore(currentHourTime);

    }

    private boolean isVehicleCapacityFilled(Vehicle vehicle) {
        return vehicle.getCurrentlyLoadedWeight() == vehicle.getKapacitetTezine() &&
                vehicle.getGetCurrentlyLoadedCapacity() == vehicle.getKapacitetProstora();
    }

    private boolean isUrgent(Paket paket) {
        return paket.getUslugaDostave().equals(TypeOfService.H.toString());
    }

}
