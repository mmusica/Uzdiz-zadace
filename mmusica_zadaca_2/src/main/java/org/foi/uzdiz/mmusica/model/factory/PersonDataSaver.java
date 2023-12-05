package org.foi.uzdiz.mmusica.model.factory;

import org.foi.uzdiz.mmusica.model.Person;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonDataSaver extends DataSaver<Person> {
    private static final String FILENAME = "po";
    private static final int NUMBER_OF_ARGS = 4;
    private static final int NAZIV = 0;
    private static final int GRAD = 1;
    private static final int ULICA = 2;
    private static final int KBR = 3;
    private final Repository<Location> placeRepository = RepositoryManager.getINSTANCE().getPlacesRepository();

    @Override
    public List<Person> createDataList() {
        List<String[]> allAttributes = this.readDataFromFile(TerminalCommandHandler.getInstance().getNewProperties().getProperty(FILENAME));
        final int[] counter = {2};
        List<Person> personList = new ArrayList<>();
        allAttributes.forEach(a -> {
            if (Arrays.stream(a).count() != NUMBER_OF_ARGS) {
                TerminalCommandHandler.getInstance().handleError(a, "Osoba: Neispravan broj argumenata u redu %d".formatted(counter[0]));

            } else {
                Person newPerson = createNewPerson(a, counter);
                if(newPerson != null){
                    personList.add(newPerson);
                }
            }
            counter[0]++;
        });
        return personList;
    }

    private Person createNewPerson(String[] a, int[] counter) {
        long idGrada = Long.parseLong(a[GRAD]);
        long idUlice = Long.parseLong(a[ULICA]);
        String name = a[NAZIV];
        int kbr = Integer.parseInt(a[KBR].trim());

        Location place = findPlace(idGrada);
        Location street = null;
        if (place != null) {
            street = place.findStreet(idUlice);
        } else {
            TerminalCommandHandler.getInstance().handleError(a, "Osoba: Grad ne postoji -> red %d".formatted(counter[0]));
            return null;
        }
        if (street == null) {
            TerminalCommandHandler.getInstance().handleError(a, "Osoba: Ulica ne postoji -> red %d".formatted(counter[0]));
            return null;
        }
        return new Person(name, place, street, kbr);
    }

    private Location findPlace(long l) {
        var allPlaces = placeRepository.getAll();
        for (Location location : allPlaces) {
            if (location.getId() == l) return location;
        }
        return null;
    }
}
