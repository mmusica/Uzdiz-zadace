package org.foi.uzdiz.mmusica.strategy;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.voznja.Drive;
import org.foi.uzdiz.mmusica.voznja.GPS;
import org.foi.uzdiz.mmusica.voznja.Segment;

import java.time.LocalDateTime;
import java.util.*;

public class ClosestDeliveryStrategy implements DeliveryStrategy{
    public boolean isSorted = false;
    @Override
    public void deliverPackage(Vehicle vehicle) {
        //zaOrderStrategijuSamoSortat pakete od vozila sortVehiclePackages(Vehicle vehicle)
        if(!isSorted && !vehicle.getPackages().stream().allMatch(Paket::isDelivered)){
            vehicle.setPackages(sortVehiclePackagesByClosest(vehicle));
        }
        var trenutnoVrijeme = TerminalCommandHandler.getInstance().getVirtualniSat().plusSeconds(0);
        Optional<Paket> currentlyInDelivery = vehicle.getPackages().stream().filter(Paket::isBeingDelivered).findFirst();
        currentlyInDelivery.ifPresent(paket -> {
            paket.setBeingDelivered(false);
            paket.setDelivered(true);
            vehicle.setMoney(vehicle.getMoney().add(paket.getVehiclePrice()));

            System.out.printf("ZAVRSENA DOSTAVA paketa u vozilu %s u virtualno vrijeme: %s%n",
                    vehicle.getOpis(), TerminalCommandHandler.getInstance().getCroDateString());
            paket.setStatusIsporuke("Dostavljen");
            unloadPackage(paket, vehicle);
            vehicle.setBrojIsporucenih(vehicle.getBrojIsporucenih() + 1);
            GPS destinationGPS = GPS.getDestinationGPS(paket);
            vehicle.setCurrentGPS(destinationGPS);
        });

        List<Paket> paketi = vehicle.getPackages().stream()
                .filter(paket -> !paket.isBeingDelivered() && !paket.isDelivered())
                .toList();

        paketi.stream().findFirst().ifPresent(paket -> {
            paket.setBeingDelivered(true);
            GPS destinationGPS = GPS.getDestinationGPS(paket);
            double udaljenostDoKuce = GPS.distanceInKM(vehicle.getCurrentGPS(), destinationGPS);
            double calcTime = (udaljenostDoKuce / vehicle.getProsjecnaBrzina()) * 60;
            long minutes = (long) calcTime;
            long seconds = (long) ((calcTime - minutes) * 60);
            //vrijeme zavrsetka je trenutno plus kolko treba da dode do kuce plus vrijeme isporuke --vi u min
            int vrijemeIsporuke = TerminalCommandHandler.getInstance().getVrijemeIsporuke();
            LocalDateTime finishTime = trenutnoVrijeme.plusMinutes(minutes).plusSeconds(seconds)
                    .plusMinutes(vrijemeIsporuke);
            vehicle.setDeliveryFinishedBy(finishTime);
            paket.setStatusIsporuke("Trenutno u isporuci");

            List<Drive> drives = vehicle.getDrives();
            Drive drive = drives.get(drives.size() - 1);
            Segment newSegment = new Segment(vehicle.getCurrentGPS(), destinationGPS, udaljenostDoKuce,
                    trenutnoVrijeme, finishTime, calcTime, vrijemeIsporuke, paket);
            drive.getSegments().add(newSegment);
        });
    }

    private List<Paket> sortVehiclePackagesByClosest(Vehicle vehicle) {
        List<Paket> returnMe = new ArrayList<>();

        //
        if(vehicle.getRegistracija().equals("VŽ100PK")){
            var debug = "debig";
        }
        for(int i=0; i<vehicle.getPackages().size(); i++){
            List<Paket> packages = vehicle.getPackages().stream().filter(paket -> !paket.isDelivered()).toList();
            GPS start = vehicle.getCurrentGPS();
            Map<Paket, Double> paketDoubleHashMap = new HashMap<>();
            for (Paket paket : packages){
                GPS finish = GPS.getDestinationGPS(paket);
                double distanceInKM = GPS.distanceInKM(start, finish);
                paketDoubleHashMap.put(paket, distanceInKM);
            }

            paketDoubleHashMap.entrySet().stream().min(Map.Entry.comparingByValue())
                    .ifPresent(paketDoubleEntry -> returnMe.add(paketDoubleEntry.getKey()));
            vehicle.setCurrentGPS(
                    GPS.getDestinationGPS(returnMe.get(returnMe.size()-1))
            );
            returnMe.get(returnMe.size()-1).setDelivered(true);
        }

        //
        this.isSorted = true;
        returnMe.forEach(paket -> paket.setDelivered(false));
        vehicle.setCurrentGPS(TerminalCommandHandler.getInstance().getOfficeGps());
        return returnMe;

    }

    @Override
    public void sendVehicleHome(Vehicle vehicle) {

        var trenutnoVrijeme = TerminalCommandHandler.getInstance().getVirtualniSat().plusSeconds(0);
        GPS destinationGPS = TerminalCommandHandler.getInstance().getOfficeGps();
        double udaljenostDoKuce = GPS.distanceInKM(vehicle.getCurrentGPS(), destinationGPS);
        double calcTime = (udaljenostDoKuce / vehicle.getProsjecnaBrzina()) * 60;
        long minutes = (long) calcTime;
        long seconds = (long) ((calcTime - minutes) * 60);
        //vrijeme zavrsetka je trenutno plus kolko treba da dode do kuce plus vrijeme isporuke --vi u min
        LocalDateTime finishTime = trenutnoVrijeme.plusMinutes(minutes).plusSeconds(seconds);
        vehicle.setDeliveryFinishedBy(finishTime);
        vehicle.setDeliveryFinishedBy(finishTime);

        List<Drive> drives = vehicle.getDrives();
        Drive drive = drives.get(drives.size() - 1);
        drive.getSegments().add(new Segment(vehicle.getCurrentGPS(), destinationGPS, udaljenostDoKuce,
                trenutnoVrijeme, finishTime, calcTime, 0, null));
        //myb refactor
        vehicle.setCurrentGPS(destinationGPS);
        isSorted = false; // bitno
    }
    private void unloadPackage(Paket paket, Vehicle vehicle) {
        double currentlyLoadedWeight = vehicle.getCurrentlyLoadedWeight();
        double getCurrentlyLoadedCapacity = vehicle.getCurrentlyLoadedCapacity();

        vehicle.setCurrentlyLoadedWeight(currentlyLoadedWeight - paket.getTezina());
        vehicle.setCurrentlyLoadedCapacity(getCurrentlyLoadedCapacity - paket.calculatePackageSize());
    }
}
