package org.foi.uzdiz.mmusica.model.factory;

import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.model.locations.Place;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceSaver extends DataSaver<Location> {
    private static final int NUMBER_OF_ARGS = 3;
    private static final int ID = 0;
    private static final int NAZIV = 1;
    private static final int ULICE = 2;

    private final Repository<Location> streetRepository = RepositoryManager.getINSTANCE().getStreetRepository();

    @Override
    public List<Location> createDataList() {
        List<String[]> allAttributes = this.readDataFromFile(TerminalCommandHandler.getInstance().getNewProperties().getProperty("pm"));
        final int[] counter = {2};
        List<Location> places = new ArrayList<>();
        allAttributes.forEach(a -> {
            if (Arrays.stream(a).count() != NUMBER_OF_ARGS) {
                TerminalCommandHandler.getInstance().handleError(a, "Mjesto: Neispravan broj argumenata u redu %d".formatted(counter[0]));

            } else{
                places.add(createNewPlace(a, counter));
            }
            counter[0]++;
        } );
        return places;
    }

    private Location createNewPlace(String[] a, int[] counter) {
        Long id = Long.parseLong(a[ID]);
        String naziv = a[NAZIV];
        List<Location> streets = new ArrayList<>();
        String[] streetIds = a[ULICE].trim().split(",");
        var allStreets = streetRepository.getAll();
        boolean found;
        for (String streetId : streetIds) {
            found = false;
           for(Location street: allStreets){
               if(street.getId() == Long.parseLong(streetId)){
                   streets.add(street);
                   found = true;
                   break;
               }
           }
           if(!found){
               TerminalCommandHandler.getInstance().handleError(a, "Mjesta, red %d: Ovakva ulica uopce ne postoji %s".formatted(counter[0],streetId));
           }
        }
        return new Place(id, naziv, streets);
    }
}

