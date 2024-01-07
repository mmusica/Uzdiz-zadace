package org.foi.uzdiz.mmusica.model.factory;

import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.model.state.ActiveVehicleState;
import org.foi.uzdiz.mmusica.model.state.BrokenVehicleState;
import org.foi.uzdiz.mmusica.model.state.InactiveVehicleState;
import org.foi.uzdiz.mmusica.model.state.VehicleState;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.strategy.SimpleStrategyFactory;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.voznja.GPS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VehicleDataSaver extends DataSaver<Vehicle> {
    private static final int REGISTRACIJA = 0;
    private static final int OPIS = 1;
    private static final int KAPACITET_TEZINE = 2;
    private static final int KAPACITET_PROSTORA = 3;
    private static final int REDOSLIJED = 4;
    private static final int PROSJECNA_BRZINA = 5;
    private static final int PODRUCJA_PO_RANGU = 6;
    private static final int STATUS = 7;
    private static final int NUMBER_OF_ARGS = 8;


    @Override
    public List<Vehicle> createDataList() {
        List<String[]> attributes = this.readDataFromFile(TerminalCommandHandler.getInstance().getPopisVozilaDokument());
        List<Vehicle> vehicles = new ArrayList<>();
        final int[] counter = {2};

        attributes.forEach(a -> {
            try {
                Vehicle vehicle = createVehicle(a, counter[0]);
                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            } catch (Exception e) {
                TerminalCommandHandler.getInstance().handleError(a, "Vozila: tekst umjesto broja");
            }
            counter[0]++;
        });
        return vehicles;
    }

    private Vehicle createVehicle(String[] a, int counter) {
        if (Arrays.stream(a).count() != NUMBER_OF_ARGS) {
            TerminalCommandHandler.getInstance().handleError(a, "Neispravan broj argumenata u redu %d".formatted(counter));
            return null;
        }

        List<Location> deliveryArea = createDeliveryArea(a);
        Vehicle vehicle = new Vehicle(a[REGISTRACIJA], a[OPIS],
                Double.parseDouble(a[KAPACITET_TEZINE].replace(',', '.')),
                Double.parseDouble(a[KAPACITET_PROSTORA].replace(',', '.')),
                Integer.parseInt(a[REDOSLIJED].replace(',', '.')), new BigDecimal(0), new ArrayList<>(),
                Float.parseFloat(a[PROSJECNA_BRZINA]), deliveryArea, getOfficeGPS());
        VehicleState vehicleState = getVehicleState(a, vehicle);
        if(vehicleState == null){
            TerminalCommandHandler.getInstance().handleError(a, "Vozilo u nepoznatom stanju, greska u redu %d".formatted(counter));
            return null;
        }

        vehicle.changeState(vehicleState);
        return vehicle;
    }

    private GPS getOfficeGPS() {

       String gps = (String) TerminalCommandHandler.getInstance().getNewProperties().get("gps");
       String[] gpsLocations = gps.split(",");
       double lat = Double.parseDouble(gpsLocations[0].trim());
       double lon = Double.parseDouble(gpsLocations[1].trim());
       return new GPS(lat,lon);
    }

    private static VehicleState getVehicleState(String[] a, Vehicle vehicle) {
       VehicleState vehicleState = null;
        switch (a[STATUS]){
           case "A":{
               SimpleStrategyFactory simpleStrategyFactory = new SimpleStrategyFactory();
               vehicleState = new ActiveVehicleState(simpleStrategyFactory.getStrategy());
               break;
           }
           case "NA":{
               vehicleState = new InactiveVehicleState();
               break;
           }
           case "NI":{
               vehicleState = new BrokenVehicleState();
               break;
           }
           default:{
               break;
           }
       }
       return vehicleState;
    }

    private List<Location> createDeliveryArea(String[] line) {
        Repository<Location> areasRepository = RepositoryManager.getINSTANCE().getAreasRepository();
        List<Location> allAreas = new ArrayList<>();
        String[] rangovi = line[PODRUCJA_PO_RANGU].split(",");
        for (String s : rangovi) {
            Location area = areasRepository.find(Long.parseLong(s));
            if (area == null) {
                TerminalCommandHandler.getInstance().handleError(line, "Ovakvo podrucje ne postoji %s".formatted(s));
            } else {
                allAreas.add(area);
            }
        }
        return allAreas;
    }

}
