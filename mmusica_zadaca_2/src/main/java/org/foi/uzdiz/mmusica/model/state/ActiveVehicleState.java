package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.strategy.DeliveryStrategy;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.voznja.Drive;
import org.foi.uzdiz.mmusica.voznja.GPS;
import org.foi.uzdiz.mmusica.voznja.Segment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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


     //       deliverPackage(currentTime);
            deliveryStrategy.deliverPackage(this.vehicle);
            if (areAllPackagesDelivered()) {
                //deliveryStrategy
               deliveryStrategy.sendVehicleHome(this.vehicle);
            }
            //jedini problem je sto se gps zove currentGps i mijenja dok vozilo tehnicki jos ne dode do ureda
            if (areAllPackagesDelivered() && vehicle.getCurrentGPS().equals(TerminalCommandHandler.getInstance().getOfficeGps())) {
                this.clearData();
            }
        }
    }
    //deliveryStrategy
    private void sendVehicleHome() {

        var trenutnoVrijeme = TerminalCommandHandler.getInstance().getVirtualniSat().plusSeconds(0);
        GPS destinationGPS = TerminalCommandHandler.getInstance().getOfficeGps();
        double udaljenostDoKuce = GPS.distanceInKM(this.vehicle.getCurrentGPS(), destinationGPS);
        double calcTime = (udaljenostDoKuce / this.vehicle.getProsjecnaBrzina()) * 60;
        long minutes = (long) calcTime;
        long seconds = (long) ((calcTime - minutes) * 60);
        //vrijeme zavrsetka je trenutno plus kolko treba da dode do kuce plus vrijeme isporuke --vi u min
        LocalDateTime finishTime = trenutnoVrijeme.plusMinutes(minutes).plusSeconds(seconds);
        this.vehicle.setDeliveryFinishedBy(finishTime);
        this.vehicle.setDeliveryFinishedBy(finishTime);

        List<Drive> drives = this.vehicle.getDrives();
        Drive drive = drives.get(drives.size() - 1);
        drive.getSegments().add(new Segment(this.vehicle.getCurrentGPS(), destinationGPS, udaljenostDoKuce,
                trenutnoVrijeme, finishTime, calcTime, 0, null));
        //myb refactor
        this.vehicle.setCurrentGPS(destinationGPS);
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
        var trenutnoVrijeme = TerminalCommandHandler.getInstance().getVirtualniSat().plusSeconds(0);
        //po prosjecnoj brzini i ukupnim putevima izracunamo kad bude gotovo
        //tu deliveryStrategy.deliverPackage();

        //deliverPackage(trenutnoVrijeme);
        deliveryStrategy.deliverPackage(this.vehicle);

        System.out.printf("Vozilo %s krenulo u isporuku i biti ce gotovo nakon %s%n", this.vehicle.getOpis(),
                vehicle.getCroatianDate(this.vehicle.getDeliveryFinishedBy()));
    }

    private void deliverPackage(LocalDateTime trenutnoVrijeme) {
        //zaOrderStrategijuSamoSortat pakete od vozila sortVehiclePackages(Vehicle vehicle)
        Optional<Paket> currentlyInDelivery = this.vehicle.getPackages().stream().filter(Paket::isBeingDelivered).findFirst();
        currentlyInDelivery.ifPresent(paket -> {
            paket.setBeingDelivered(false);
            paket.setDelivered(true);
            this.vehicle.setMoney(this.vehicle.getMoney().add(paket.getVehiclePrice()));

            System.out.printf("ZAVRSENA DOSTAVA paketa u vozilu %s u virtualno vrijeme: %s%n",
                    this.vehicle.getOpis(), TerminalCommandHandler.getInstance().getCroDateString());
            paket.setStatusIsporuke("Dostavljen");
            unloadPackage(paket);
            this.vehicle.setBrojIsporucenih(this.vehicle.getBrojIsporucenih()+1);
            GPS destinationGPS = GPS.getDestinationGPS(paket);
            this.vehicle.setCurrentGPS(destinationGPS);
        });

        List<Paket> paketi = this.vehicle.getPackages().stream()
                .filter(paket -> !paket.isBeingDelivered() && !paket.isDelivered())
                .toList();

        paketi.stream().findFirst().ifPresent(paket -> {
            paket.setBeingDelivered(true);
            GPS destinationGPS = GPS.getDestinationGPS(paket);
            double udaljenostDoKuce = GPS.distanceInKM(this.vehicle.getCurrentGPS(), destinationGPS);
            double calcTime = (udaljenostDoKuce / this.vehicle.getProsjecnaBrzina()) * 60;
            long minutes = (long) calcTime;
            long seconds = (long) ((calcTime - minutes) * 60);
            //vrijeme zavrsetka je trenutno plus kolko treba da dode do kuce plus vrijeme isporuke --vi u min
            int vrijemeIsporuke = TerminalCommandHandler.getInstance().getVrijemeIsporuke();
            LocalDateTime finishTime = trenutnoVrijeme.plusMinutes(minutes).plusSeconds(seconds)
                    .plusMinutes(vrijemeIsporuke);
            this.vehicle.setDeliveryFinishedBy(finishTime);
            paket.setStatusIsporuke("Trenutno u isporuci");

            List<Drive> drives = this.vehicle.getDrives();
            Drive drive = drives.get(drives.size() - 1);
            Segment newSegment = new Segment(this.vehicle.getCurrentGPS(), destinationGPS, udaljenostDoKuce,
                    trenutnoVrijeme, finishTime, calcTime, vrijemeIsporuke, paket);
            drive.getSegments().add(newSegment);
        });
    }

    private void unloadPackage(Paket paket) {
        double currentlyLoadedWeight = this.vehicle.getCurrentlyLoadedWeight();
        double getCurrentlyLoadedCapacity = this.vehicle.getCurrentlyLoadedCapacity();

        this.vehicle.setCurrentlyLoadedWeight(currentlyLoadedWeight - paket.getTezina());
        this.vehicle.setCurrentlyLoadedCapacity(getCurrentlyLoadedCapacity - paket.calculatePackageSize());
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
