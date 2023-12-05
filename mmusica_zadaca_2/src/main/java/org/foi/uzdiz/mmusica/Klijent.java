package org.foi.uzdiz.mmusica;

import org.foi.uzdiz.mmusica.facade.DataFacade;
import org.foi.uzdiz.mmusica.facade.impl.DataFacadeImpl;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.UserCommandHandler;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Klijent {
    public static void main(String[] args) {
        String command = getCommand(args);

        DataFacade dataFacade = new DataFacadeImpl();
        dataFacade.initializeDataWithCommand(command);

        var lol = RepositoryManager.getINSTANCE().getAreasRepository();
        var lol2 = RepositoryManager.getINSTANCE().getVehicleRepository();
        var lol3 = RepositoryManager.getINSTANCE().getPackageRepository();
        var lol4 = RepositoryManager.getINSTANCE().getPersonRepository();
        askForUserInput(new UserCommandHandler());
    }

    private static void askForUserInput(UserCommandHandler userCommandHandler) {
        String command;
        do {
            Scanner myObj = new Scanner(System.in);
            command = myObj.nextLine();
            String response = userCommandHandler.handleUserCommand(command);
            Logger.getGlobal().log(Level.INFO, response);
        } while (!command.equalsIgnoreCase("Q"));

    }

    public static String getCommand(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : args) {
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder.toString().trim();
    }

}