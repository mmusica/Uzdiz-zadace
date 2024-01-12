package org.foi.uzdiz.mmusica.utils.decorator;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.util.List;

public class IPOutputerImpl implements IPOutputer{
    @Override
    public String outputIPCommand() {
        StringBuilder sb = new StringBuilder();

        String formattedString = String.format("TRENUTNO VRIJEME: %s%n", TerminalCommandHandler.getInstance().getCroDateString());
        sb.append(formattedString);
        sb.append("\nPrimljeni\n");
        formattedString = String.format("%-5s | %-10s | %-10s | %-12s | %-12s | %-25s | %-20s | %-20s%n", "VRSTA", "USLUGA", "OZNAKA",
                "DOSTAVA", "POUZECE (V)", "STATUS", "VRIJEME PRIJEMA", "VRIJEME PREUZIMANJA");
        sb.append(formattedString);
        List<Paket> all = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        List<Paket> primljeniPaketi = all.stream().filter(paket -> paket.isReceived() && !paket.isDelivered()).toList();
        List<Paket> dostavljeniPaketi = all.stream().filter(Paket::isDelivered).toList();
        primljeniPaketi.forEach(paket -> sb.append(paket.getPackageString()));
        sb.append("\nGotovi\n");
        formattedString = String.format("%-5s | %-10s | %-10s | %-12s | %-12s | %-25s | %-20s | %-20s%n", "VRSTA", "USLUGA",
                "OZNAKA", "DOSTAVA", "POUZECE", "STATUS", "VRIJEME PRIJEMA", "VRIJEME PREUZIMANJA");
        sb.append(formattedString);
        dostavljeniPaketi.forEach(paket -> sb.append(paket.getPackageString()));
        return sb.toString();
    }
}
