package org.foi.uzdiz.mmusica.strategy;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.voznja.GPS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClosestDeliveryStrategy implements DeliveryStrategy{
    public boolean isSorted = false;
    public PackageHandler packageHandler = new PackageHandler();
    @Override
    public void deliverPackages(Vehicle vehicle) {
        if(!isSorted && !vehicle.getPackages().stream().allMatch(Paket::isDelivered)){
            vehicle.setPackages(sortVehiclePackagesByClosest(vehicle));
        }
       packageHandler.deliverPackage(vehicle);
    }

    @Override
    public void sendVehicleHome(Vehicle vehicle) {

        packageHandler.sendVehicleHome(vehicle);
        isSorted = false; // bitno
    }

    private List<Paket> sortVehiclePackagesByClosest(Vehicle vehicle) {
        List<Paket> returnMe = new ArrayList<>();

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

        this.isSorted = true;
        returnMe.forEach(paket -> paket.setDelivered(false));
        vehicle.setCurrentGPS(TerminalCommandHandler.getInstance().getOfficeGps());
        return returnMe;
    }
}
