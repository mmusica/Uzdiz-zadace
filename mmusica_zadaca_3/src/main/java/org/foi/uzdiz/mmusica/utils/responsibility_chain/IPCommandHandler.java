package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.util.List;

public class IPCommandHandler extends UserCommandHandler {
    public IPCommandHandler(UserCommandHandler next) {
        super(next);
    }
    @Override
    public void executeCommand(String[] commandArray) {

        System.out.printf("TRENUTNO VRIJEME: %s%n", TerminalCommandHandler.getInstance().getCroDateString());

        System.out.println("\nPrimljeni");
        System.out.printf("%-5s | %-10s | %-10s | %-12s | %-12s | %-25s | %-20s | %-20s%n", "VRSTA", "USLUGA", "OZNAKA",
                "DOSTAVA", "POUZECE (V)", "STATUS", "VRIJEME PRIJEMA", "VRIJEME PREUZIMANJA");
        List<Paket> all = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        List<Paket> primljeniPaketi = all.stream().filter(paket -> paket.isReceived() && !paket.isDelivered()).toList();
        List<Paket> dostavljeniPaketi = all.stream().filter(Paket::isDelivered).toList();
        primljeniPaketi.forEach(Paket::soutPackage);
        System.out.println("\nDostavljeni");
        System.out.printf("%-5s | %-10s | %-10s | %-12s | %-12s | %-25s | %-20s | %-20s%n", "VRSTA", "USLUGA",
                "OZNAKA", "DOSTAVA", "POUZECE", "STATUS", "VRIJEME PRIJEMA", "VRIJEME PREUZIMANJA");

        dostavljeniPaketi.forEach(Paket::soutPackage);
    }
    @Override
    String getCommand() {
        return "IP";
    }
}
