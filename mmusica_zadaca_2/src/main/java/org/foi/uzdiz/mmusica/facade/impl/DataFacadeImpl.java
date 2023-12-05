package org.foi.uzdiz.mmusica.facade.impl;

import org.foi.uzdiz.mmusica.facade.DataFacade;
import org.foi.uzdiz.mmusica.model.factory.*;
import org.foi.uzdiz.mmusica.parameter_handler.ParameterHandler;
import org.foi.uzdiz.mmusica.parameter_handler.ParameterLoader;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;

import java.util.Properties;

public class DataFacadeImpl implements DataFacade {

    private final RepositoryManager repositoryManager = RepositoryManager.getINSTANCE();

    public DataFacadeImpl() {
    }

    @Override
    public void initializeData() {

        setRepoData(new StreetDataSaver(), RepositoryManager.getINSTANCE().getStreetRepository());
        setRepoData(new PlaceDataSaver(), RepositoryManager.getINSTANCE().getPlacesRepository());
        setRepoData(new AreaDataSaver(), RepositoryManager.getINSTANCE().getAreasRepository());

        //Osoba mora imat lokacije
        setRepoData(new PersonDataSaver(), RepositoryManager.getINSTANCE().getPersonRepository());
        setRepoData(new PackageTypeDataSaver(), RepositoryManager.getINSTANCE().getPackageTypeRepository());
        //Paket mora imati packageType
        setRepoData(new PackageDataSaver(), RepositoryManager.getINSTANCE().getPackageRepository());
        setRepoData(new VehicleDataSaver(), RepositoryManager.getINSTANCE().getVehicleRepository());
    }

    @Override
    public void initializeCommandData(String command) {
        ParameterLoader parameterLoader = new ParameterLoader();
        Properties properties = parameterLoader.loadProperties(command);
        ParameterHandler parameterHandler = new ParameterHandler();
        parameterHandler.handleProperties(properties);
    }

    public static <T> void setRepoData(DataSaver<T> dataSaver, Repository<T> locationRepository) {
        dataSaver.repositorySave(locationRepository);
    }
}
