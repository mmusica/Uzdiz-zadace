package org.foi.uzdiz.mmusica.repository;

import org.foi.uzdiz.mmusica.model.PackageType;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;

public class RepositoryManager {
    private static final RepositoryManager INSTANCE;
    private Repository<Paket> packageRepository;
    private Repository<PackageType> packageTypeRepository;
    private Repository<Vehicle> vehicleRepository;


    static {
        INSTANCE = new RepositoryManager(
                new PackageRepository(),
                new PackageTypeRepository(),
                new VehicleRepository()
        );
    }

    private RepositoryManager(Repository<Paket> packageRepository,
                             Repository<PackageType> packageTypeRepository,
                             Repository<Vehicle> vehicleRepository) {

        this.packageRepository = packageRepository;
        this.packageTypeRepository = packageTypeRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public static RepositoryManager getINSTANCE() {
        return INSTANCE;
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
}
