package org.foi.uzdiz.mmusica.model.factory;

import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.model.locations.Street;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StreetSaver extends DataSaver<Location> {
    private static final int NUMBER_OF_ARGS = 7;
    private static final int ID = 0;
    private static final int NAZIV = 1;
    private static final int LAT_1 = 2;
    private static final int LON_1 = 3;
    private static final int LAT_2 = 4;
    private static final int LON_2 = 5;
    private static final int NAJVECI_KUCNI_BROJ = 6;

    @Override
    public List<Location> createDataList() {
        List<String[]> allAttributes = this.readDataFromFile(TerminalCommandHandler.getInstance().getNewProperties().getProperty("pu"));
        final int[] counter = {2};
        List<Location> streets = new ArrayList<>();
        allAttributes.forEach(a -> {
            if (Arrays.stream(a).count() != NUMBER_OF_ARGS) {
                TerminalCommandHandler.getInstance().handleError(a, "Ulice: Neispravan broj argumenata u redu %d".formatted(counter[0]));

            } else {
                streets.add(new Street(Long.parseLong(a[ID]), a[NAZIV],
                        Float.parseFloat(a[LAT_1]), Float.parseFloat(a[LON_1]),
                        Float.parseFloat(a[LAT_2]), Float.parseFloat(a[LON_2]),
                        Integer.parseInt(a[NAJVECI_KUCNI_BROJ])));
            }
            counter[0]++;
        });
        return streets;
    }
}
