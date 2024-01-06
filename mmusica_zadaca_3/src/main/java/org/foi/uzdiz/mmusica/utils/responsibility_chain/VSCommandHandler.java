package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.visitor.SegmentDataDisplayVisitor;

public class VSCommandHandler extends UserCommandHandler {
    public VSCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {
        if (commandArray.length != 3) {
            System.out.println("Krivi broj argumenata");
            return;
        }
        System.out.printf("TRENUTNO VRIJEME: %s%n", TerminalCommandHandler.getInstance().getCroDateString());
        System.out.printf("%-25s | %-25s | %-20s | %-20s | %-12s%n", "VRIJEME POC", "VRIJEME POVR", "TRAJANJE", "UKUPNO KM", "OZNAKA PAKETA");
        Vehicle vehicle = RepositoryManager.getINSTANCE().getVehicleRepository().find(commandArray[1]);
        if (vehicle == null) {
            System.out.println("Ovakvo vozilo ne postoji");
            return;
        }
        int index = Integer.parseInt(commandArray[2]);
        vehicle.accept(new SegmentDataDisplayVisitor(index));
    }

    @Override
    String getCommand() {
        return "VS";
    }
}
