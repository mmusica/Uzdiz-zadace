package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.model.state.ActiveVehicleState;
import org.foi.uzdiz.mmusica.model.state.BrokenVehicleState;
import org.foi.uzdiz.mmusica.model.state.InactiveVehicleState;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.strategy.SimpleStrategyFactory;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PSCommandHandler extends UserCommandHandler {
    final String regexPs = "^PS\\s[^\\s]+(?:\\sA|\\sNI|\\sNA)$";
    private static final int VEHICLE_ID = 1;
    private static final int VEHICLE_STATE = 2;
    final Pattern patternPs = Pattern.compile(regexPs);

    public PSCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(commandArray).toList().forEach(s -> stringBuilder.append(s).append(" "));
        String command = stringBuilder.toString().trim();
        if (isPromjenaStanjaCommandValid(command)) {
            handlePromjenaStanja(commandArray);
        }
    }

    @Override
    String getCommand() {
        return "PS";
    }

    private boolean isPromjenaStanjaCommandValid(String command) {
        final Matcher matcher = patternPs.matcher(command);
        return matcher.matches();
    }

    private void handlePromjenaStanja(String[] commandArray) {
        Vehicle vozilo = RepositoryManager.getINSTANCE().getVehicleRepository().find(commandArray[VEHICLE_ID]);
        if (vozilo != null) {
            voziloChangeState(vozilo, commandArray[VEHICLE_STATE]);
        } else {
            Logger.getGlobal().log(Level.SEVERE, "Vozilo ne postoji");
        }
    }

    private void voziloChangeState(Vehicle vozilo, String s) {

        SimpleStrategyFactory simpleStrategyFactory = new SimpleStrategyFactory();
        switch (s) {
            case "A" -> vozilo.changeState(new ActiveVehicleState(simpleStrategyFactory.getStrategy()));
            case "NI" -> vozilo.changeState(new BrokenVehicleState());
            case "NA" -> vozilo.changeState(new InactiveVehicleState());
        }
    }
}
