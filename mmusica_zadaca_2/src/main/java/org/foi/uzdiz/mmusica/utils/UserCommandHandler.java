package org.foi.uzdiz.mmusica.utils;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.model.state.ActiveVehicleState;
import org.foi.uzdiz.mmusica.model.state.BrokenVehicleState;
import org.foi.uzdiz.mmusica.model.state.InactiveVehicleState;
import org.foi.uzdiz.mmusica.observer.Observer;
import org.foi.uzdiz.mmusica.office.ReceptionOffice;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.visitor.VehicleDataDisplayVisitor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCommandHandler {
    private static final String VIRTUALNO_VRIJEME = "VR";
    private static final String ISPIS = "IP";
    private static final String QUIT = "Q";
    private static final String  PREGLED_PODRUCJA = "PP";
    private static final String  PROMJENA_OBAVIJESTI = "PO";
    private static final String FAIL_RESPONSE = "Nepoznata naredba";
    private static final String VIRTUALNO_VRIJEME_FAIL_RESPONSE = "VR naredba nije valjana";
    private static final String SUCCESS_RESPONSE = "Naredba uspjesno izvrsena!";
    private static final String PROMJENA_STANJA = "PS";
    private static final int VEHICLE_ID = 1;
    private static final int VEHICLE_STATE = 2;

    private static final String STATUS_VOZILA = "SV";
    private final ReceptionOffice receptionOffice = new ReceptionOffice();
    final String regexPo = "^PO '([A-Za-z ]+)' \\S+ [ND]$";
    final String regexPs = "^PS\\s[^\\s]+(?:\\sA|\\sNI|\\sNA)$";
    final Pattern patternPs = Pattern.compile(regexPs);
    final Pattern patternPo = Pattern.compile(regexPo);


    public String handleUserCommand(String command) {
        String[] commandArray = command.split(" ");
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
            case STATUS_VOZILA:{
                System.out.println("STATUS|\tUKUPNO KM|\tBROJ PAKETA|\tZAUZETOST|\tBROJ VOZNJI");
                RepositoryManager.getINSTANCE().getVehicleRepository()
                        .getAll()
                        .forEach(vehicle -> vehicle.accept(new VehicleDataDisplayVisitor()));
                break;
            }

            case QUIT: {
                break;
            }
            case PREGLED_PODRUCJA: {
                displayLocations();
                break;
            }
            case PROMJENA_OBAVIJESTI:{
                if(isPromjenaObavijestiCommandValid(command)){
                    Observer observer = RepositoryManager.getINSTANCE().getPersonRepository().find(getPersonName(command));
                    Paket paket = RepositoryManager.getINSTANCE().getPackageRepository().find(getPackageId(commandArray));
                    String statusUpdate = getStatusUpdate(commandArray);
                    if(paket==null || observer==null){
                        Logger.getGlobal().log(Level.SEVERE, "Paket ili osoba ne postoje");
                    }else{
                        updateObserver(statusUpdate, paket, observer);
                    }
                }else{
                    return FAIL_RESPONSE;
                }
                break;
            }
            case PROMJENA_STANJA:{
                if(isPromjenaStanjaCommandValid(command)){
                    Vehicle vozilo = RepositoryManager.getINSTANCE().getVehicleRepository().find(commandArray[VEHICLE_ID]);
                    if(vozilo!=null){
                        voziloChangeState(vozilo, commandArray[VEHICLE_STATE]);
                    }else{
                        Logger.getGlobal().log(Level.SEVERE, "Vozilo ne postoji");
                    }
                }else {
                    return FAIL_RESPONSE;
                }
                break;
            }
            default: {
                return FAIL_RESPONSE;
            }
        }
        return SUCCESS_RESPONSE;
    }

    private void voziloChangeState(Vehicle vozilo, String s) {
        switch (s) {
            case "A" -> vozilo.changeState(new ActiveVehicleState(vozilo));
            case "NI" -> vozilo.changeState(new BrokenVehicleState(vozilo));
            case "NA" -> vozilo.changeState(new InactiveVehicleState(vozilo));
        }
    }

    private boolean isPromjenaStanjaCommandValid(String command) {
        final Matcher matcher = patternPs.matcher(command);
        return matcher.matches();
    }

    private String getStatusUpdate(String[] commandArray) {
        int statusIndex = commandArray.length-1;
        return commandArray[statusIndex];
    }

    private String getPackageId(String[] commandArray) {
        int packageIndex = commandArray.length-2;
        return commandArray[packageIndex];
    }

    private String getPersonName(String command) {
        final Matcher matcher = patternPs.matcher(command);
        if(matcher.matches()) return matcher.group(1);
        return null;
    }

    private void updateObserver(String statusUpdate, Paket paket, Observer observer) {
        if(statusUpdate.equals("D")){
            paket.registerObserver(observer);
        }else{
            paket.removeObserver(observer);
        }
    }

    private boolean isPromjenaObavijestiCommandValid(String command) {
        final Matcher matcher = patternPo.matcher(command);
        return matcher.matches();
    }

    private void displayLocations() {
        RepositoryManager.getINSTANCE().getAreasRepository().getAll().forEach(Location::display);
    }


    private void handeIspisCommand() {
        System.out.printf("TRENUTNO VRIJEME: %s%n", TerminalCommandHandler.getInstance().getCroDateString());

        System.out.println("\nPrimljeni");
        List<Paket> all = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        List<Paket> primljeniPaketi = all.stream().filter(paket -> paket.isReceived() && !paket.isDelivered()).toList();
        List<Paket> dostavljeniPaketi = all.stream().filter(Paket::isDelivered).toList();
        primljeniPaketi.forEach(System.out::println);
        System.out.println("\nDostavljeni");
        dostavljeniPaketi.forEach(System.out::println);
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
