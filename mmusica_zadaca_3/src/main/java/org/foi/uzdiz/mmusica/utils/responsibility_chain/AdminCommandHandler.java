package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.model.Person;

public class AdminCommandHandler  extends UserCommandHandler{
    public AdminCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {
        Person personFromAdminCommand = getPersonFromAdminCommand(commandArray);
        if (personFromAdminCommand != null) {
            personFromAdminCommand.setAdmin(true);
            System.out.println("Osoba je sada Admin");
        } else {
            System.out.println("Osoba ne postoji");
        }
    }

    @Override
    String getCommand() {
        return "ADMIN";
    }
}
