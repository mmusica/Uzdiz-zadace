package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.visitor.DrivesDataDisplayVisitor;

public class VVCommandHandler extends UserCommandHandler{
    public VVCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {

        if (commandArray.length != 2) {
            System.out.println("Krivi broj argumenata");
            return;
        }
        System.out.printf("TRENUTNO VRIJEME: %s%n", TerminalCommandHandler.getInstance().getCroDateString());
        System.out.printf("%-25s | %-25s | %-20s | %-20s | %-12s%n", "VRIJEME POC", "VRIJEME POVR", "TRAJANJE", "UKUPNO KM", "BROJ PAKETA U VOZILU");
        Vehicle vehicle = RepositoryManager.getINSTANCE().getVehicleRepository().find(commandArray[1]);
        if (vehicle == null) {
            System.out.println("Ovakvo vozilo ne postoji");
            return;
        }
        vehicle.accept(new DrivesDataDisplayVisitor());
    }

    @Override
    String getCommand() {
        return "VV";
    }
}
