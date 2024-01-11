package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.model.Person;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class UserCommandHandler {
    protected UserCommandHandler next;

    public void handleCommand(String[] commandArray) {
        if (commandArray[0].equalsIgnoreCase(getCommand())) {
            this.executeCommand(commandArray);
        } else {
            if (next != null) {
                next.handleCommand(commandArray);
            }else {
                if (!commandArray[0].equalsIgnoreCase("Q")) {
                    Logger.getGlobal().log(Level.WARNING, "Unesena nepoznata/neispavna naredba");
                }
            }
        }
    }

    public UserCommandHandler(UserCommandHandler next) {
        this.next = next;
    }

    protected Person getPersonFromAdminCommand(String[] commandArray) {
        StringBuilder osoba = new StringBuilder();
        for (int i = 1; i < commandArray.length; i++) {
            osoba.append(commandArray[i]).append(" ");
        }
        String osobaString = osoba.toString().trim();
        return RepositoryManager.getINSTANCE().getPersonRepository().find(osobaString);
    }

    public void setNext(UserCommandHandler next) {
        this.next = next;
    }

    abstract void executeCommand(String[] commandArray);

    abstract String getCommand();
}
