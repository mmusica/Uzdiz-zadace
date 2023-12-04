package org.foi.uzdiz.mmusica.model.factory;

import org.foi.uzdiz.mmusica.model.locations.Area;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AreaSaver extends DataSaver<Location> {
    private static final String FILENAME = "pmu";
    private static final int NUMBER_OF_ARGS = 2;
    private static final int ID = 0;
    private static final int GRADOVI_ULICE = 1;
    private static final int STREET_INDEX = 1;
    private static final int PLACE_INDEX = 0;

    private final Repository<Location> placeRepository = RepositoryManager.getINSTANCE().getPlacesRepository();

    @Override
    public List<Location> createDataList() {
        List<String[]> allAttributes = this.readDataFromFile(TerminalCommandHandler.getInstance().getNewProperties().getProperty(FILENAME));
        final int[] counter = {2};
        List<Location> areas = new ArrayList<>();
        allAttributes.forEach(a -> {
            if (Arrays.stream(a).count() != NUMBER_OF_ARGS) {
                TerminalCommandHandler.getInstance().handleError(a, "Podrucja: Neispravan broj argumenata u redu %d".formatted(counter[0]));

            } else{
                areas.add(createNewArea(a, counter));
            }
            counter[0]++;
        } );
        return areas;
    }

    private Location createNewArea(String[] a, int[] counter) {
        Long id = Long.parseLong(a[ID]);
        List<Location> locations = new ArrayList<>();
        String[] cityStreetId = a[GRADOVI_ULICE].trim().split(",");

        for(String s : cityStreetId){
            String[] pair = s.split(":");
            Location place = findPlace(Long.parseLong(pair[PLACE_INDEX]));
            if(place == null){
                TerminalCommandHandler.getInstance().handleError(a, "Podrucja, red %d: Ovakvo mjesto uopce ne postoji %s".formatted(counter[0], pair[PLACE_INDEX]));
                continue;
            }
            if(pair[STREET_INDEX].equals("*")){
                 locations.add(place);
            }else {
                Location street = place.findStreet(Long.parseLong(pair[STREET_INDEX]));
                if(street == null){
                    TerminalCommandHandler.getInstance().handleError
                            (a, "Podrucja, red %d: Ovakva ulica uopce ne postoji %s".formatted(counter[0], pair[STREET_INDEX]));
                }else{
                    locations.add(street);
                }
            }
        }
        return new Area(id, locations);
    }

    private Location findPlace(long l) {
        var allPlaces = placeRepository.getAll();
        for(Location location : allPlaces){
            if(location.getId()==l) return location;
        }
        return null;
    }
}
