package org.foi.uzdiz.mmusica;

import org.foi.uzdiz.mmusica.data_extractor.DataExtractor;
import org.foi.uzdiz.mmusica.data_extractor.PackageDataExtractor;
import org.foi.uzdiz.mmusica.data_extractor.PackageTypeDataExtractor;
import org.foi.uzdiz.mmusica.data_extractor.VehicleDataExtractor;
import org.foi.uzdiz.mmusica.parameter_handler.ParameterHandler;
import org.foi.uzdiz.mmusica.parameter_handler.ParameterLoader;
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

        TerminalCommandHandler.getInstance().setCommandData(parameterHandler.handleParameters(properties));

        initializeData(new PackageTypeDataExtractor());
        initializeData(new PackageDataExtractor());
        initializeData(new VehicleDataExtractor());

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
    public static void initializeData(DataExtractor dataExtractor){
        dataExtractor.extractData();
    }
}