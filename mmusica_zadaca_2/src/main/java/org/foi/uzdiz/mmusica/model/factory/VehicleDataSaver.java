package org.foi.uzdiz.mmusica.model.factory;

import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

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
                TerminalCommandHandler.getInstance().handleError(a,"Vozila: tekst umjesto broja");
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
        String[] rangovi = a[PODRUCJA_PO_RANGU].split(",");
        return new Vehicle(a[REGISTRACIJA], a[OPIS],
                Double.parseDouble(a[KAPACITET_TEZINE].replace(',', '.')),
                Double.parseDouble(a[KAPACITET_PROSTORA].replace(',', '.')),
                Integer.parseInt(a[REDOSLIJED].replace(',', '.')),new BigDecimal(0), new ArrayList<>(),
                Float.parseFloat(a[PROSJECNA_BRZINA]),rangovi,a[STATUS]);
    }

}