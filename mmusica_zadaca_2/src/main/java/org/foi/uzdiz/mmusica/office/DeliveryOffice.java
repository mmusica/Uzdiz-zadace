package org.foi.uzdiz.mmusica.office;

import org.foi.uzdiz.mmusica.enums.TypeOfService;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
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
        while (!receivedPackages.isEmpty() && !areAllVehiclesDriving ){
            vehicles.forEach(vehicle ->{
                List<Paket> loadedPackages = getLoadedPackages(vehicle, receivedPackages);
                if(!loadedPackages.isEmpty()){
                    receivedPackages.removeAll(loadedPackages);
                    logLoadedPackages(loadedPackages, vehicle);
                }
            });
            if(!receivedPackages.isEmpty()) break;
        }
        return receivedPackages;
    }
    private void logLoadedPackages(List<Paket> packages, Vehicle vehicle) {
        for (Paket p : packages) {
            System.out.printf("VRIJEME %s: Ukrcan paket s oznakom %s hitnosti %s na vozilo %s%n", TerminalCommandHandler.getInstance().getCroDateString(), p.getOznaka(), p.getUslugaDostave(), vehicle.getOpis());
        }
    }
    private List<Paket> getLoadedPackages(Vehicle vehicle, List<Paket> receivedPackages) {
        List<Paket> loadedPackages = new ArrayList<>();
        for (Paket paket : receivedPackages) {
            if(paket.isErrored()) continue;
            if (isUrgentAndNotBeingDelivered(paket) && hasEnoughCapacity(vehicle, paket) && hasEnoughWeight(vehicle, paket)  && !paket.isErrored()) {
                Paket loadedPackage = vehicle.loadPackageIntoVehicle(paket);
                if(loadedPackage!=null){
                    loadedPackages.add(loadedPackage);
                }
            }
        }
        for (Paket paket : receivedPackages) {
            if (hasEnoughCapacity(vehicle, paket) && hasEnoughWeight(vehicle, paket) && !paket.isBeingDelivered() && !paket.isErrored()) {
                Paket loadedPackage = vehicle.loadPackageIntoVehicle(paket);
                if(loadedPackage!=null){
                    loadedPackages.add(loadedPackage);
                }
            }
        }
        return loadedPackages;
    }
    public void deliverPackages() {
        //Nakon što se popuni kapacitet vozila
        // ili nakon punog sata vozilo koje ima barem jedan ukrcani paket hitne dostave
        // ili je popunjeno minimalno 50% kapaciteta (težine ili prostora) kreće prema odredištima paketa.
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isDriving()) vehicle.finalizeDeliveries();

            if ((isVehicleCapacityFilled(vehicle) || isHourLaterWithUrgentPackage(vehicle)
                    || is50percentFull(vehicle)) && !vehicle.isDriving() && !vehicle.getPackages().isEmpty()) {
                vehicle.startDeliveringPackages();
            }
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
    private boolean isVehicleCapacityFilled(Vehicle vehicle) {
        return vehicle.getCurrentlyLoadedWeight() == vehicle.getKapacitetTezine() &&
                vehicle.getGetCurrentlyLoadedCapacity() == vehicle.getKapacitetProstora();
    }
    private boolean isUrgentAndNotBeingDelivered(Paket paket) {
        return paket.getUslugaDostave().equals(TypeOfService.H.toString()) && !paket.isBeingDelivered();
    }
    private boolean hasEnoughCapacity(Vehicle vehicle, Paket paket) {
        double capacity = vehicle.getKapacitetProstora() - vehicle.getGetCurrentlyLoadedCapacity();
        double packageSize = paket.calculatePackageSize();
        return packageSize <= capacity;
    }
    private boolean hasEnoughWeight(Vehicle vehicle, Paket paket) {
        double capacity = vehicle.getKapacitetTezine() - vehicle.getCurrentlyLoadedWeight();
        double packageWeight = paket.getTezina();
        return packageWeight <= capacity;
    }
}
