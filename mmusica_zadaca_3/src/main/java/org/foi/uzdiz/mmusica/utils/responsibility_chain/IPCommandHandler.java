package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.decorator.IPOutputer;

public class IPCommandHandler extends UserCommandHandler {
    public IPCommandHandler(UserCommandHandler next) {
        super(next);
    }
    @Override
    public void executeCommand(String[] commandArray) {
        IPOutputer ipOutputer = RepositoryManager.getINSTANCE().getIpOutputer();
        System.out.println(ipOutputer.outputIPCommand());
    }
    @Override
    String getCommand() {
        return "IP";
    }
}
