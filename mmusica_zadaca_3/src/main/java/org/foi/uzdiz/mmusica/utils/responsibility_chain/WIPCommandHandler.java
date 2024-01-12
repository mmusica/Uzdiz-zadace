package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.decorator.IPOutputer;
import org.foi.uzdiz.mmusica.utils.decorator.WIPDecorator;

public class WIPCommandHandler extends UserCommandHandler {
    public WIPCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {
        IPOutputer ipOutputer = RepositoryManager.getINSTANCE().getIpOutputer();
        ipOutputer = new WIPDecorator(ipOutputer);
        RepositoryManager.getINSTANCE().setIpOutputer(ipOutputer);
        System.out.println("Naredba IP je prosirena s korisnicima!");
    }

    @Override
    String getCommand() {
        return "WIP";
    }
}
