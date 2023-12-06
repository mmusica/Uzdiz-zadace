package org.foi.uzdiz.mmusica.office;

import org.foi.uzdiz.mmusica.enums.TypeOfPackage;
import org.foi.uzdiz.mmusica.enums.TypeOfService;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DeliveryOffice {
    public final Deque<Vehicle> vehicles = new ArrayDeque<>();
    private LocalDateTime fullHourPassed = TerminalCommandHandler.getInstance().getVirtualniSat().plusHours(1);
    private final Repository<Vehicle> vehicleRepository = RepositoryManager.getINSTANCE().getVehicleRepository();

    public DeliveryOffice() {
        vehicles.addAll(vehicleRepository.getAll());
    }

    public List<Paket> loadPackages(List<Paket> receivedPackages) {
        boolean areAllVehiclesDriving = vehicles.stream().allMatch(Vehicle::isDriving);
        while (!receivedPackages.isEmpty() && !areAllVehiclesDriving ){
            vehicles.forEach(vehicle ->{
                List<Paket> packagesToLoad = getPackagesToLoad(vehicle, receivedPackages);
                if(!packagesToLoad.isEmpty()){
                    receivedPackages.removeAll(packagesToLoad);
                    vehicle.getPackages().addAll(packagesToLoad);
                    logLoadedPackages(packagesToLoad, vehicle);
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

    private List<Paket> getPackagesToLoad(Vehicle vehicle, List<Paket> receivedPackages) {
        List<Paket> packagesToLoad = new ArrayList<>();

        for (Paket paket : receivedPackages) {
            if(paket.isErrored()) continue;
            if (isUrgentAndNotBeingDelivered(paket) && hasEnoughCapacity(vehicle, paket) && hasEnoughWeight(vehicle, paket)  && !paket.isErrored()) {
                vehicle.setGetCurrentlyLoadedCapacity(vehicle.getGetCurrentlyLoadedCapacity() + calculatePackageSize(paket));
                vehicle.setCurrentlyLoadedWeight(vehicle.getCurrentlyLoadedWeight() + paket.getTezina());
                paket.setBeingDelivered(true);
                paket.setStatusIsporuke("Ukrcan u vozilo");
                packagesToLoad.add(paket);
            }
        }
        for (Paket paket : receivedPackages) {
            if (hasEnoughCapacity(vehicle, paket) && hasEnoughWeight(vehicle, paket) && !paket.isBeingDelivered() && !paket.isErrored()) {
                vehicle.setGetCurrentlyLoadedCapacity(vehicle.getGetCurrentlyLoadedCapacity() + calculatePackageSize(paket));
                vehicle.setCurrentlyLoadedWeight(vehicle.getCurrentlyLoadedWeight() + paket.getTezina());
                paket.setBeingDelivered(true);
                paket.setStatusIsporuke("Ukrcan u vozilo");
                packagesToLoad.add(paket);
            }
        }
        return packagesToLoad;
    }

    private boolean isUrgentAndNotBeingDelivered(Paket paket) {
        return paket.getUslugaDostave().equals(TypeOfService.H.toString()) && !paket.isBeingDelivered();
    }

    private boolean hasEnoughCapacity(Vehicle vehicle, Paket paket) {
        double capacity = vehicle.getKapacitetProstora() - vehicle.getGetCurrentlyLoadedCapacity();
        double packageSize = 0;
        packageSize = calculatePackageSize(paket);
        return packageSize <= capacity;
    }

    private static double calculatePackageSize(Paket paket) {
        double packageSize;
        if (paket.getVrstaPaketa().getOznaka().equals(TypeOfPackage.X.toString())) {
            packageSize = paket.getVisina() * paket.getSirina() * paket.getDuzina();
        } else {
            packageSize = paket.getVrstaPaketa().getDuzina() * paket.getVrstaPaketa().getVisina() * paket.getVrstaPaketa().getSirina();
        }
        return packageSize;
    }

    private boolean hasEnoughWeight(Vehicle vehicle, Paket paket) {
        double capacity = vehicle.getKapacitetTezine() - vehicle.getCurrentlyLoadedWeight();
        double packageWeight = 0;
        packageWeight = paket.getTezina();
        if (packageWeight <= capacity) {
            return true;
        }
        return false;
    }

    public void deliverPackages() {
        //Nakon što se popuni kapacitet vozila
        // ili nakon punog sata vozilo koje ima barem jedan ukrcani paket hitne dostave
        // ili je popunjeno minimalno 50% kapaciteta (težine ili prostora) kreće prema odredištima paketa.
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isDriving()) finalizeDeliveries(vehicle);
            if ((isVehicleCapacityFilled(vehicle) || isHourLaterWithUrgentPackage(vehicle) || is50percentFull(vehicle)) && !vehicle.isDriving() && !vehicle.getPackages().isEmpty()) {
                vehicle.setDriving(true);
                vehicle.setDeliveryFinishedBy(TerminalCommandHandler.getInstance().getVirtualniSat()
                        .plusMinutes(TerminalCommandHandler.getInstance().getVrijemeIsporuke()));
                vehicle.getPackages().forEach(paket -> {
                    paket.setBeingDelivered(true);
                    paket.setStatusIsporuke("Trenutno u isporuci");
                });
                System.out.printf("Vozilo %s krenulo u isporuku i biti ce gotovo nakon %s%n", vehicle.getOpis(),
                        getCroatianDate(vehicle.getDeliveryFinishedBy()));
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

    private String getCroatianDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return dateTime.format(formatter);
    }

    private void finalizeDeliveries(Vehicle vehicle) {
        LocalDateTime currentTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        if (vehicle.isDriving() && (vehicle.getDeliveryFinishedBy().isEqual(currentTime) || vehicle.getDeliveryFinishedBy().isBefore(currentTime))) {
            vehicle.getPackages().forEach(paket -> {
                paket.setBeingDelivered(false);
                paket.setDelivered(true);
                vehicle.setMoney(vehicle.getMoney().add(paket.getVehiclePrice()));
                paket.setStatusIsporuke("Dostavljen");

            });
            clearVehicleData(vehicle);
            System.out.printf("ZAVRSENA DOSTAVA vozila %s u virtualno vrijeme: %s%n",
                    vehicle.getOpis(), TerminalCommandHandler.getInstance().getCroDateString());

        }
    }

    private void clearVehicleData(Vehicle vehicle) {
        vehicle.getPackages().clear();
        vehicle.setCurrentlyLoadedWeight(0);
        vehicle.setGetCurrentlyLoadedCapacity(0);
        vehicle.setDriving(false);
        vehicle.setDeliveryFinishedBy(null);
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
}
