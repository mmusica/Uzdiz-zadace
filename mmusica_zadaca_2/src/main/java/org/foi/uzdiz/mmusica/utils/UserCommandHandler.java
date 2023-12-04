package org.foi.uzdiz.mmusica.utils;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.office.ReceptionOffice;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;

import java.util.List;

public class UserCommandHandler {
    private static final String VIRTUALNO_VRIJEME = "VR";
    private static final String ISPIS = "IP";
    private static final String QUIT = "Q";
    private static final String FAIL_RESPONSE = "Nepoznata naredba";
    private static final String VIRTUALNO_VRIJEME_FAIL_RESPONSE = "VR naredba nije valjana";
    private static final String SUCCESS_RESPONSE = "Naredba uspjesno izvrsena!";

    private final ReceptionOffice receptionOffice = new ReceptionOffice();

    public String handleUserCommand(String command) {
        String[] commandArray = command.split(" ");
        String response = null;
        switch (commandArray[0].toUpperCase()) {
            case VIRTUALNO_VRIJEME: {
                if (isVirtualnoVrijemeValid(commandArray)) {
                    receptionOffice.receivePackage(commandArray[1]);
                } else return VIRTUALNO_VRIJEME_FAIL_RESPONSE;
                break;
            }
            case ISPIS: {
                handeIspisCommand();
                break;
            }
            case QUIT: {
                break;
            }
            default: {
                return FAIL_RESPONSE;
            }
        }
        return SUCCESS_RESPONSE;
    }


    private void handeIspisCommand() {
        System.out.println("TRENUTNO VRIJEME: %s".formatted(TerminalCommandHandler.getInstance().getCroDateString()));

        System.out.println("\nPrimljeni");
        List<Paket> all = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        List<Paket> primljeniPaketi = all.stream().filter(paket -> paket.isReceived() && !paket.isDelivered()).toList();
        List<Paket> dostavljeniPaketi = all.stream().filter(Paket::isDelivered).toList();
        primljeniPaketi.forEach(System.out::println);
        //TablePrinter.printTable(primljeniPaketi);
        System.out.println("\nDostavljeni");
        dostavljeniPaketi.forEach(System.out::println);
        //TablePrinter.printTable(dostavljeniPaketi);
    }

    private boolean isVirtualnoVrijemeValid(String[] commandArray) {
        int value;
        try {
            value = Integer.parseInt(commandArray[1]);
        } catch (Exception ex) {
            return false;
        }
        return value > 0 && value < 100;
    }
}
