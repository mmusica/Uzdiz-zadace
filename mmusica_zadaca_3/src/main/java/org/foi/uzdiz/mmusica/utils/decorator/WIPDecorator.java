package org.foi.uzdiz.mmusica.utils.decorator;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;

import java.util.List;

public class WIPDecorator extends IPDecorator {
    private final IPOutputer ipOutputer;

    public WIPDecorator(IPOutputer ipOutputer) {
        this.ipOutputer = ipOutputer;
    }

    @Override
    public String outputIPCommand() {
        return getWho() + ipOutputer.outputIPCommand();
    }

    private String getWho() {
        List<Paket> pakets = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        StringBuilder sb = new StringBuilder();
        String title = String.format("%n%-10s | %-20s | %-20s%n", "OZNAKA", "POSILJATELJ", "PRIMATELJ");
        sb.append(title);
        pakets.forEach(paket -> sb.append(paket.getWIPPackageString()));
        return sb.toString();
    }
}
