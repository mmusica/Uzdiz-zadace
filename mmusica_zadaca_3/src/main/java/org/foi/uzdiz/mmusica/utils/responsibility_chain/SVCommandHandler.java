package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;
import org.foi.uzdiz.mmusica.visitor.VehicleDataDisplayVisitor;

public class SVCommandHandler extends UserCommandHandler {
    public SVCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {
        if(commandArray.length != 1){
            System.out.println("Neispravan broj argumenata");
            return;
        }
        System.out.printf("TRENUTNO VRIJEME: %s%n", TerminalCommandHandler.getInstance().getCroDateString());
        System.out.printf("%-15s | %-15s | %-20s | %-12s | %-12s %n", "STATUS", "UKUPNO KM", "ZAUZETOST(%)"
                , "BROJ VOZNJI", "BROJ PAKETA");
        RepositoryManager.getINSTANCE().getVehicleRepository()
                .getAll()
                .forEach(vehicle -> vehicle.accept(new VehicleDataDisplayVisitor()));
    }

    @Override
    String getCommand() {
        return "SV";
    }
}
