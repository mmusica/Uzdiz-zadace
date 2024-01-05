package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.voznja.Drive;
import org.foi.uzdiz.mmusica.voznja.GPS;
import org.foi.uzdiz.mmusica.voznja.Segment;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ActiveVehicleState implements VehicleState {
    private final Vehicle vehicle;

    //private final DeliveryStrategy deliveryStrategy;
    public ActiveVehicleState(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public void finalizeDeliveries() {
        LocalDateTime currentTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        if (this.vehicle.isDriving() && (this.vehicle.getDeliveryFinishedBy().isEqual(currentTime)
                || this.vehicle.getDeliveryFinishedBy().isBefore(currentTime))) {
            System.out.printf("ZAVRSENA DOSTAVA vozila %s u virtualno vrijeme: %s%n",
                    this.vehicle.getOpis(), TerminalCommandHandler.getInstance().getCroDateString());


            deliverPackage(currentTime);
            if (areAllPackagesDelivered()) {
                sendVehicleHome();
            }
            //jedini problem je sto se gps zove currentGps i mijenja dok vozilo tehnicki jos ne dode do ureda
            if (areAllPackagesDelivered() && vehicle.getCurrentGPS().equals(TerminalCommandHandler.getInstance().getOfficeGps())) {
                this.clearData();
            }
        }
    }

    private void sendVehicleHome() {

        var trenutnoVrijeme = TerminalCommandHandler.getInstance().getVirtualniSat().plusSeconds(0);
        GPS destinationGPS = TerminalCommandHandler.getInstance().getOfficeGps();
        double udaljenostDoKuce = GPS.distance(this.vehicle.getCurrentGPS(), destinationGPS);
        long time = (long) (udaljenostDoKuce / vehicle.getProsjecnaBrzina());
        LocalDateTime finishTime = trenutnoVrijeme.plusHours(time);
        this.vehicle.setDeliveryFinishedBy(finishTime);

        List<Drive> drives = this.vehicle.getDrives();
        Drive drive = drives.get(drives.size() - 1);
        drive.getSegments().add(new Segment(this.vehicle.getCurrentGPS(), destinationGPS, udaljenostDoKuce,
                trenutnoVrijeme, finishTime, time, 0, null));
        //myb refactor
        this.vehicle.setCurrentGPS(destinationGPS);
    }

    private boolean areAllPackagesDelivered() {
        return this.vehicle.getPackages().stream().allMatch(Paket::isDelivered);
    }


    @Override
    public Paket loadPackageIntoVehicle(Paket paket) {
        if (hasEnoughCapacity(vehicle, paket) && hasEnoughWeight(vehicle, paket)
                && !paket.isBeingDelivered() && !paket.isErrored()) {

            this.vehicle.setGetCurrentlyLoadedCapacity(this.vehicle.getGetCurrentlyLoadedCapacity() + paket.calculatePackageSize());
            this.vehicle.setCurrentlyLoadedWeight(this.vehicle.getCurrentlyLoadedWeight() + paket.getTezina());

            //paket.setBeingDelivered(true);
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
        //
        var trenutnoVrijeme = TerminalCommandHandler.getInstance().getVirtualniSat().plusSeconds(0);
        this.vehicle.setDeliveryStarted(trenutnoVrijeme);
        this.vehicle.setVrijemePovratka(null);
        //
        //po prosjecnoj brzini i ukupnim putevima izracunamo kad bude gotovo

        deliverPackage(trenutnoVrijeme);

        System.out.printf("Vozilo %s krenulo u isporuku i biti ce gotovo nakon %s%n", this.vehicle.getOpis(),
                vehicle.getCroatianDate(this.vehicle.getDeliveryFinishedBy()));
    }

    private void deliverPackage(LocalDateTime trenutnoVrijeme) {

        Optional<Paket> currentlyInDelivery = this.vehicle.getPackages().stream().filter(Paket::isBeingDelivered).findFirst();
        currentlyInDelivery.ifPresent(paket -> {
            paket.setBeingDelivered(false);
            paket.setDelivered(true);
            this.vehicle.setMoney(this.vehicle.getMoney().add(paket.getVehiclePrice()));
            paket.setStatusIsporuke("Dostavljen");

            GPS destinationGPS = getDestinationGPS(paket);
            this.vehicle.setCurrentGPS(destinationGPS);
        });

        List<Paket> paketi = this.vehicle.getPackages().stream()
                .filter(paket -> !paket.isBeingDelivered() && !paket.isDelivered())
                .toList();

        paketi.stream().findFirst().ifPresent(paket -> {
            paket.setBeingDelivered(true);
            GPS destinationGPS = getDestinationGPS(paket);
            double udaljenostDoKuce = GPS.distance(this.vehicle.getCurrentGPS(), destinationGPS);
            long time = (long) (udaljenostDoKuce / vehicle.getProsjecnaBrzina());
            //vrijeme zavrsetka je trenutno plus kolko treba da dode do kuce plus vrijeme isporuke --vi u min
            int vrijemeIsporuke = TerminalCommandHandler.getInstance().getVrijemeIsporuke();
            LocalDateTime finishTime = trenutnoVrijeme.plusHours(time)
                    .plusMinutes(vrijemeIsporuke);
            this.vehicle.setDeliveryFinishedBy(finishTime);
            paket.setStatusIsporuke("Trenutno u isporuci");

            List<Drive> drives = this.vehicle.getDrives();
            Drive drive = drives.get(drives.size() - 1);
            drive.getSegments().add(new Segment(this.vehicle.getCurrentGPS(), destinationGPS, udaljenostDoKuce,
                    trenutnoVrijeme, finishTime, time, vrijemeIsporuke, paket));

        });
    }

    @Override
    public void clearData() {
        this.vehicle.getPackages().clear();
        this.vehicle.setCurrentlyLoadedWeight(0);
        this.vehicle.setGetCurrentlyLoadedCapacity(0);
        this.vehicle.setDriving(false);
        this.vehicle.setDeliveryFinishedBy(null);
    }

    @Override
    public String giveStatus() {
        return "Aktivan";
    }

    // c = duljinaUlice, a = lon2-lon1,  b = lat2-lat1
    // c' (manjiTrokut) = duljinaDoKuce, a'(manjiTrokut), b'(manjiTrokut)
    //            .
    //      c   .  |
    //      .  |   |
    //   .     |   | a
    // ._______|___|
    //      b

    private GPS getDestinationGPS(Paket paket) {
        Location destinationStreet = paket.getDestinationStreet();
        Long id = destinationStreet.getId();

        GPS startOfStreet = destinationStreet.getStartOfStreet(id);
        if (paket.getKbrPrimatelja() == 1) {
            return startOfStreet;
        }
        GPS endOfStreet = destinationStreet.getEndOfStreet(id);
        if (paket.getKbrPrimatelja() == paket.getNajveciKbrUlicePrimatelja()) {
            return endOfStreet;
        }

        double postotak = (double) paket.getKbrPrimatelja() / paket.getNajveciKbrUlicePrimatelja();
        double duljinaUlice = GPS.distance(startOfStreet, endOfStreet);
        double duljinaDoKuce;
        if (paket.getKbrPrimatelja() > paket.getNajveciKbrUlicePrimatelja()) {
            duljinaDoKuce = duljinaUlice;
        } else {
            duljinaDoKuce = duljinaUlice * postotak;
        }
        double lat1 = startOfStreet.getLat();
        double lon1 = startOfStreet.getLon();

        double lat2 = endOfStreet.getLat();
        double lon2 = endOfStreet.getLon();

        double a = Math.abs(Math.max(lon2, lon1) - Math.min(lon2, lon1));
//        double b = Math.abs(Math.max(lat2, lat1) - Math.min(lat2, lat1));
        double sinAlpha = a / duljinaUlice;

        double aManji = duljinaDoKuce * sinAlpha;
        double bManji = Math.sqrt(Math.pow(duljinaDoKuce, 2) - Math.pow(aManji, 2));

        double latDestination = lat1 + bManji;
        double lonDestination = lon1 + aManji;
        return new GPS(latDestination, lonDestination);
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
