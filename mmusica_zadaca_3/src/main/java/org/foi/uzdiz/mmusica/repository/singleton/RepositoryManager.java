package org.foi.uzdiz.mmusica.repository.singleton;

import org.foi.uzdiz.mmusica.model.PackageType;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Person;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.repository.*;
import org.foi.uzdiz.mmusica.utils.decorator.IPOutputer;
import org.foi.uzdiz.mmusica.utils.decorator.IPOutputerImpl;

public class RepositoryManager {
    private static final RepositoryManager INSTANCE;
    private Repository<Paket> packageRepository;
    private Repository<PackageType> packageTypeRepository;
    private Repository<Vehicle> vehicleRepository;
    private Repository<Location> areasRepository;
    private Repository<Location> placesRepository;
    private Repository<Location> streetRepository;
    private Repository<Person> personRepository;
    private IPOutputer ipOutputer;

    static {
        INSTANCE = new RepositoryManager(
                new PackageRepository(),
                new PackageTypeRepository(),
                new VehicleRepository(),
                new LocationRepository(),
                new LocationRepository(),
                new LocationRepository(),
                new PersonRepository(),
                new IPOutputerImpl()
        );
    }

    public RepositoryManager(Repository<Paket> packageRepository, Repository<PackageType> packageTypeRepository,
                             Repository<Vehicle> vehicleRepository, Repository<Location> areasRepository,
                             Repository<Location> placesRepository, Repository<Location> streetRepository,
                             Repository<Person> personRepository, IPOutputer ipOutputer) {
        this.packageRepository = packageRepository;
        this.packageTypeRepository = packageTypeRepository;
        this.vehicleRepository = vehicleRepository;
        this.areasRepository = areasRepository;
        this.placesRepository = placesRepository;
        this.streetRepository = streetRepository;
        this.personRepository = personRepository;
        this.ipOutputer = ipOutputer;
    }

    public static RepositoryManager getINSTANCE() {
        return INSTANCE;
    }
    public Repository<Location> getAreasRepository() {
        return areasRepository;
    }
    public Repository<Location> getPlacesRepository() {
        return placesRepository;
    }
    public Repository<Location> getStreetRepository() {
        return streetRepository;
    }
    public Repository<Paket> getPackageRepository() {
        return packageRepository;
    }
    public Repository<PackageType> getPackageTypeRepository() {
        return packageTypeRepository;
    }
    public Repository<Vehicle> getVehicleRepository() {
        return vehicleRepository;
    }
    public Repository<Person> getPersonRepository() {
        return personRepository;
    }

    public void setIpOutputer(IPOutputer ipOutputer) {
        this.ipOutputer = ipOutputer;
    }

    public IPOutputer getIpOutputer() {
        return ipOutputer;
    }
}
