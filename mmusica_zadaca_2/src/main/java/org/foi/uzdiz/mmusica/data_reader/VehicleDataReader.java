package org.foi.uzdiz.mmusica.data_reader;

import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.RepositoryManager;
import org.foi.uzdiz.mmusica.repository.VehicleRepository;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VehicleDataReader extends DataReader {
    private static final int REGISTRACIJA = 0;
    private static final int OPIS = 1;
    private static final int KAPACITET_TEZINE = 2;
    private static final int KAPACITET_PROSTORA = 3;
    private static final int REDOSLIJED = 4;
    private static final int NUMBER_OF_ARGS = 5;

    private final Repository<Vehicle> vehicleRepository = RepositoryManager.getINSTANCE().getVehicleRepository();


    @Override
    public void saveData() {
        List<String[]> attributes = this.readDataFromFile(TerminalCommandHandler.getInstance().getPopisVozilaDokument());
        final int[] counter = {2};
        attributes.forEach(a -> {
            try {
                Vehicle vehicle = createVehicle(a, counter[0]);
                if (vehicle != null) {
                    vehicleRepository.save(vehicle);
                }
            } catch (Exception e) {
                TerminalCommandHandler.getInstance().handleError(a,"Vozila: tekst umjesto broja");
            }
            counter[0]++;
        });
    }

    private Vehicle createVehicle(String[] a, int counter) {
        if (Arrays.stream(a).count() != NUMBER_OF_ARGS) {
            TerminalCommandHandler.getInstance().handleError(a, "Neispravan broj argumenata u redu %d".formatted(counter));
            return null;
        }
        return new Vehicle(a[REGISTRACIJA], a[OPIS],
                Double.parseDouble(a[KAPACITET_TEZINE].replace(',', '.')),
                Double.parseDouble(a[KAPACITET_PROSTORA].replace(',', '.')),
                Integer.parseInt(a[REDOSLIJED].replace(',', '.')),new BigDecimal(0), new ArrayList<>());
    }

}
