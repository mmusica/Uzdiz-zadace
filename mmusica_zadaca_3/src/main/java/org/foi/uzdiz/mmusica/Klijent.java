package org.foi.uzdiz.mmusica;

import org.foi.uzdiz.mmusica.facade.DataFacade;
import org.foi.uzdiz.mmusica.facade.impl.DataFacadeImpl;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.UserCommandHandlerClient;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Klijent {
    public static void main(String[] args) {
        String command = getCommand(args);
        DataFacade dataFacade = new DataFacadeImpl();
        dataFacade.initializeDataWithCommand(command);
        askForUserInput(new UserCommandHandlerClient());
    }

    private static void askForUserInput(UserCommandHandlerClient userCommandHandlerClient) {
        String command;
        do {
            Scanner myObj = new Scanner(System.in);
            command = myObj.nextLine();
            userCommandHandlerClient.handleUserCommand(command);
            System.out.println("--------------------------------------------------------------------------------------");
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