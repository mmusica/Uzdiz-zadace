package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.memento.SystemSnapShotCaretaker;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;

import java.util.List;

public class SPVCommandHandler extends UserCommandHandler{
    public SPVCommandHandler(UserCommandHandler next) {
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
        String realSaveWord = saveWord.substring(1,saveWord.length()-2);
        List<Paket> paketi = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        List<Vehicle> vehicles = RepositoryManager.getINSTANCE().getVehicleRepository().getAll();
        SystemSnapShotCaretaker.getInstance().takeCurrentSystemSnapshot(saveWord,paketi,vehicles);
    }

    @Override
    String getCommand() {
        return "SPV";
    }
}
