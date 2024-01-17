package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.observer.Observer;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POCommandHandler extends UserCommandHandler{
    static final String regexPo = "^PO '(.+)' \\S+ [ND]$";
    final Pattern patternPo = Pattern.compile(regexPo);
    public POCommandHandler(UserCommandHandler next) {
        super(next);
    }
    @Override
    void executeCommand(String[] commandArray) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(commandArray).toList().forEach(s -> stringBuilder.append(s).append(" "));
        String command = stringBuilder.toString().trim();

        if (isPromjenaObavijestiCommandValid(command)) {
            handlePromjenaObavijesti(command, commandArray);
        }
    }
    @Override
    String getCommand() {
        return "PO";
    }
    private boolean isPromjenaObavijestiCommandValid(String command) {
        final Matcher matcher = patternPo.matcher(command);
        return matcher.matches();
    }
    private void handlePromjenaObavijesti(String command, String[] commandArray) {
        Observer observer = RepositoryManager.getINSTANCE().getPersonRepository().find(getPersonName(command));
        Paket paket = RepositoryManager.getINSTANCE().getPackageRepository().find(getPackageId(commandArray));
        String statusUpdate = getStatusUpdate(commandArray);
        if (paket == null || observer == null) {
            Logger.getGlobal().log(Level.SEVERE, "Paket ili osoba ne postoje");
        } else {
            updateObserver(statusUpdate, paket, observer);
        }
    }
    private String getPersonName(String command) {
        final Matcher matcher = patternPo.matcher(command);
        if (matcher.matches()) return matcher.group(1);
        return null;
    }
    private String getPackageId(String[] commandArray) {
        int packageIndex = commandArray.length - 2;
        return commandArray[packageIndex];
    }
    private String getStatusUpdate(String[] commandArray) {
        int statusIndex = commandArray.length - 1;
        return commandArray[statusIndex];
    }
    private void updateObserver(String statusUpdate, Paket paket, Observer observer) {
        if (statusUpdate.equals("D")) {
            paket.registerObserver(observer);
        } else {
            paket.removeObserver(observer);
        }
    }
}
