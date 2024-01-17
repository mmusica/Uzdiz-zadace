package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.memento.SystemSnapShotCaretaker;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PPVCommandHandler extends UserCommandHandler{
    public PPVCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {

        if(commandArray.length !=2 ){
            System.out.println("Nevaljan broj agrumenata");
            return;
        }
        String saveWord = commandArray[1];
        char c = saveWord.charAt(0);
        char c2 = saveWord.charAt(saveWord.length()-1);
        if(c != '\'' || c2 != '\''){
            System.out.println("Neispravan format naredbe");
            return;
        }
        List<Paket> paketi = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        List<Vehicle> vehicles = RepositoryManager.getINSTANCE().getVehicleRepository().getAll();
        SystemSnapShotCaretaker.getInstance().restoreCurrentSystem(saveWord,paketi,vehicles);
        Logger.getGlobal().log(Level.INFO, "Stanje sustava uspjesno vraceno");
    }

    @Override
    String getCommand() {
        return "PPV";
    }
}
