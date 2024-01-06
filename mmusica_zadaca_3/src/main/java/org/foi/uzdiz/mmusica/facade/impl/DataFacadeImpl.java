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
    public void initializeDataWithCommand(String command) {
        this.initializeCommandDataParameters(command);
        this.initializeData();
    }

    private void initializeCommandDataParameters(String command) {
        ParameterLoader parameterLoader = new ParameterLoader();
        Properties properties = parameterLoader.loadProperties(command);
        ParameterHandler parameterHandler = new ParameterHandler();
        parameterHandler.handleProperties(properties);
    }

    public void initializeData() {

        setRepoData(new StreetDataSaver(), repositoryManager.getStreetRepository());
        setRepoData(new PlaceDataSaver(), repositoryManager.getPlacesRepository());
        setRepoData(new AreaDataSaver(), repositoryManager.getAreasRepository());

        //Osoba mora imat lokacije
        setRepoData(new PersonDataSaver(), repositoryManager.getPersonRepository());
        setRepoData(new PackageTypeDataSaver(), repositoryManager.getPackageTypeRepository());
        //Paket mora imati packageType
        setRepoData(new PackageDataSaver(), repositoryManager.getPackageRepository());
        setRepoData(new VehicleDataSaver(), repositoryManager.getVehicleRepository());
    }
    public static <T> void setRepoData(DataSaver<T> dataSaver, Repository<T> locationRepository) {
        dataSaver.repositorySave(locationRepository);
    }
}
