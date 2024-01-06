package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;

public class PPCommandHandler extends UserCommandHandler{
    public PPCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {
        RepositoryManager.getINSTANCE().getAreasRepository().getAll().forEach(Location::display);
    }

    @Override
    String getCommand() {
        return "PP";
    }
}
