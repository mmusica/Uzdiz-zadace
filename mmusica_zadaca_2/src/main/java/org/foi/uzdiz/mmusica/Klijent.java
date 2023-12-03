package org.foi.uzdiz.mmusica;

import org.foi.uzdiz.mmusica.model.factory.*;
import org.foi.uzdiz.mmusica.parameter_handler.ParameterHandler;
import org.foi.uzdiz.mmusica.parameter_handler.ParameterLoader;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.utils.UserCommandHandler;

import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Klijent {
    public static void main(String[] args) {
        String command = getCommand(args);
        ParameterLoader parameterLoader = new ParameterLoader();

        Properties properties = parameterLoader.loadProperties(command);
        ParameterHandler parameterHandler = new ParameterHandler();
        parameterHandler.handleProperties(properties);

        Properties newProperties = TerminalCommandHandler.getInstance().getNewProperties();

        initializeData(new PackageTypeSaver(), RepositoryManager.getINSTANCE().getPackageTypeRepository());
        initializeData(new PackageSaver(), RepositoryManager.getINSTANCE().getPackageRepository());
        initializeData(new VehicleSaver(), RepositoryManager.getINSTANCE().getVehicleRepository());

        initializeData(new StreetSaver(), RepositoryManager.getINSTANCE().getStreetRepository());
        initializeData(new PlaceSaver(), RepositoryManager.getINSTANCE().getPlacesRepository());
        initializeData(new AreaSaver(), RepositoryManager.getINSTANCE().getAreasRepository());

        var lol = RepositoryManager.getINSTANCE().getAreasRepository();
        var lol2 = RepositoryManager.getINSTANCE().getVehicleRepository();
        var lol3 = RepositoryManager.getINSTANCE().getPackageRepository();
        askForUserInput(new UserCommandHandler());
    }

    private static void askForUserInput(UserCommandHandler userCommandHandler) {
        String command;
        do {
            Scanner myObj = new Scanner(System.in);
            command = myObj.nextLine();
            String response = userCommandHandler.handleUserCommand(command);
            Logger.getGlobal().log(Level.INFO, response);
        }while(!command.equalsIgnoreCase("Q"));

    }

    public static String getCommand(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : args) {
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder.toString().trim();
    }
    public static <T> void initializeData(DataSaver<T> dataSaver, Repository<T> locationRepository){
        dataSaver.repositorySave(locationRepository);
    }
}