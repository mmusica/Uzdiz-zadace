package org.foi.uzdiz.mmusica.repository.singleton;

import org.foi.uzdiz.mmusica.model.PackageType;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Person;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.repository.*;

public class RepositoryManager {
    private static final RepositoryManager INSTANCE;
    private Repository<Paket> packageRepository;
    private Repository<PackageType> packageTypeRepository;
    private Repository<Vehicle> vehicleRepository;
    private Repository<Location> areasRepository;
    private Repository<Location> placesRepository;
    private Repository<Location> streetRepository;
    private Repository<Person> personRepository;


    static {
        INSTANCE = new RepositoryManager(
                new PackageRepository(),
                new PackageTypeRepository(),
                new VehicleRepository(),
                new LocationRepository(),
                new LocationRepository(),
                new LocationRepository(),
                new PersonRepository()
        );
    }

    public RepositoryManager(Repository<Paket> packageRepository, Repository<PackageType> packageTypeRepository,
                             Repository<Vehicle> vehicleRepository, Repository<Location> areasRepository,
                             Repository<Location> placesRepository, Repository<Location> streetRepository,
                             Repository<Person> personRepository) {
        this.packageRepository = packageRepository;
        this.packageTypeRepository = packageTypeRepository;
        this.vehicleRepository = vehicleRepository;
        this.areasRepository = areasRepository;
        this.placesRepository = placesRepository;
        this.streetRepository = streetRepository;
        this.personRepository = personRepository;
    }

    public static RepositoryManager getINSTANCE() {
        return INSTANCE;
    }

    public Repository<Location> getAreasRepository() {
        return areasRepository;
    }

    public void setAreasRepository(Repository<Location> areasRepository) {
        this.areasRepository = areasRepository;
    }

    public Repository<Location> getPlacesRepository() {
        return placesRepository;
    }

    public void setPlacesRepository(Repository<Location> placesRepository) {
        this.placesRepository = placesRepository;
    }

    public Repository<Location> getStreetRepository() {
        return streetRepository;
    }

    public void setStreetRepository(Repository<Location> streetRepository) {
        this.streetRepository = streetRepository;
    }

    public Repository<Paket> getPackageRepository() {
        return packageRepository;
    }

    public void setPackageRepository(Repository<Paket> packageRepository) {
        this.packageRepository = packageRepository;
    }

    public Repository<PackageType> getPackageTypeRepository() {
        return packageTypeRepository;
    }

    public void setPackageTypeRepository(Repository<PackageType> packageTypeRepository) {
        this.packageTypeRepository = packageTypeRepository;
    }

    public Repository<Vehicle> getVehicleRepository() {
        return vehicleRepository;
    }

    public void setVehicleRepository(Repository<Vehicle> vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Repository<Person> getPersonRepository() {
        return personRepository;
    }

    public void setPersonRepository(Repository<Person> personRepository) {
        this.personRepository = personRepository;
    }
}
