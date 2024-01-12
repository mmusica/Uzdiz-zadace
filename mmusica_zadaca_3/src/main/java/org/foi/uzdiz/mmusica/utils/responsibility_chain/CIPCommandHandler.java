package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.decorator.CIPDecorator;
import org.foi.uzdiz.mmusica.utils.decorator.IPOutputer;

public class CIPCommandHandler extends UserCommandHandler {
    public CIPCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {
        IPOutputer ipOutputer = RepositoryManager.getINSTANCE().getIpOutputer();
        ipOutputer = new CIPDecorator(ipOutputer);
        RepositoryManager.getINSTANCE().setIpOutputer(ipOutputer);
        System.out.println("Naredba IP je dekorirana!");
    }

    @Override
    String getCommand() {
        return "CIP";
    }
}
