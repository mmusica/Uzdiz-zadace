package org.foi.uzdiz.mmusica.utils;

import org.foi.uzdiz.mmusica.Proxy.PackageSubscriber;
import org.foi.uzdiz.mmusica.Proxy.PackageSubscriberImpl;
import org.foi.uzdiz.mmusica.Proxy.PackageSubscriberProxy;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Person;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.model.state.ActiveVehicleState;
import org.foi.uzdiz.mmusica.model.state.BrokenVehicleState;
import org.foi.uzdiz.mmusica.model.state.InactiveVehicleState;
import org.foi.uzdiz.mmusica.observer.Observer;
import org.foi.uzdiz.mmusica.office.ReceptionOffice;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.visitor.DrivesDataDisplayVisitor;
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
    private static final String PREGLED_PODRUCJA = "PP";
    private static final String PROMJENA_OBAVIJESTI = "PO";
    private static final String FAIL_RESPONSE = "Nepoznata naredba";
    private static final String VIRTUALNO_VRIJEME_FAIL_RESPONSE = "VR naredba nije valjana";
    private static final String SUCCESS_RESPONSE = "Naredba uspjesno izvrsena!";
    private static final String PROMJENA_STANJA = "PS";
    private static final int VEHICLE_ID = 1;
    private static final int VEHICLE_STATE = 2;
    private static final String VOZNJE_VOZILA = "VV";
    private static final String STATUS_VOZILA = "SV";
    private final ReceptionOffice receptionOffice = new ReceptionOffice();
    final String regexPo = "^PO '(.+)' \\S+ [ND]$";
    final String regexPs = "^PS\\s[^\\s]+(?:\\sA|\\sNI|\\sNA)$";
    final Pattern patternPs = Pattern.compile(regexPs);
    final Pattern patternPo = Pattern.compile(regexPo);


    //Moja implementacija za proxy
    private static final String ADMIN_COMMAND = "ADMIN";
    private static final String PROXY_COMMAND = "PROXY";
    private static final String UNPROXY_COMMAND = "UNPROXY";


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
            case STATUS_VOZILA: {
                handleStatusVozila();
                break;
            }
            case VOZNJE_VOZILA: {
                if (commandArray.length != 2) {
                    System.out.println("Krivi broj argumenata");
                    break;
                }
                handleVoznjeVozila(commandArray);
                break;
            }
            case QUIT: {
                break;
            }
            case PREGLED_PODRUCJA: {
                displayLocations();
                break;
            }
            case PROMJENA_OBAVIJESTI: {
                if (isPromjenaObavijestiCommandValid(command)) {
                    handlePromjenaObavijesti(command, commandArray);
                } else {
                    return FAIL_RESPONSE;
                }
                break;
            }
            case PROMJENA_STANJA: {
                if (isPromjenaStanjaCommandValid(command)) {
                    handlePromjenaStanja(commandArray);
                } else {
                    return FAIL_RESPONSE;
                }
                break;
            }
            default: {
                return FAIL_RESPONSE;
            }
            case ADMIN_COMMAND: {
                Person personFromAdminCommand = getPersonFromAdminCommand(commandArray);
                if (personFromAdminCommand != null) {
                    personFromAdminCommand.setAdmin(true);
                } else {
                    System.out.println("Osoba ne postoji");
                }
                break;
            }
            case PROXY_COMMAND: {
                Person personFromProxyCommand = getPersonFromAdminCommand(commandArray);
                if (personFromProxyCommand != null) {
                    subscribeToAllPackages(personFromProxyCommand);
                } else {
                    System.out.println("Osoba ne postoji");
                }
                break;
            }

            case UNPROXY_COMMAND: {
                Person personFromProxyCommand = getPersonFromAdminCommand(commandArray);
                if (personFromProxyCommand != null) {
                    unsubscribeToAllPackages(personFromProxyCommand);
                } else {
                    System.out.println("Osoba ne postoji");
                }
                break;
            }
        }
        return SUCCESS_RESPONSE;
    }

    private void unsubscribeToAllPackages(Person personFromProxyCommand) {
        PackageSubscriber packageSubscriber = new PackageSubscriberProxy(new PackageSubscriberImpl());
        List<Paket> allPackages = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        allPackages.forEach(paket -> {
            packageSubscriber.unsubscribe(paket, personFromProxyCommand);
        });
    }

    private void subscribeToAllPackages(Person person) {
        PackageSubscriber packageSubscriber = new PackageSubscriberProxy(new PackageSubscriberImpl());
        List<Paket> allPackages = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        allPackages.forEach(paket -> {
            packageSubscriber.subscribe(paket, person);
        });
    }

    private Person getPersonFromAdminCommand(String[] commandArray) {
        StringBuilder osoba = new StringBuilder();
        for (int i = 1; i < commandArray.length; i++) {
            osoba.append(commandArray[i]).append(" ");
        }
        String osobaString = osoba.toString().trim();
        return RepositoryManager.getINSTANCE().getPersonRepository().find(osobaString);
    }

    private void handlePromjenaStanja(String[] commandArray) {
        Vehicle vozilo = RepositoryManager.getINSTANCE().getVehicleRepository().find(commandArray[VEHICLE_ID]);
        if (vozilo != null) {
            voziloChangeState(vozilo, commandArray[VEHICLE_STATE]);
        } else {
            Logger.getGlobal().log(Level.SEVERE, "Vozilo ne postoji");
        }
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

    private void handleVoznjeVozila(String[] commandArray) {
        System.out.printf("TRENUTNO VRIJEME: %s%n", TerminalCommandHandler.getInstance().getCroDateString());
        System.out.printf("%-25s | %-25s | %-12s | %-20s | %-12s%n", "VRIJEME POC", "VRIJEME POVR", "TRAJANJE", "UKUPNO KM", "BROJ PAKETA U VOZILU");
        Vehicle vehicle = RepositoryManager.getINSTANCE().getVehicleRepository().find(commandArray[1]);
        if (vehicle == null) {
            System.out.println("Ovakvo vozilo ne postoji");
            return;
        }
        vehicle.accept(new DrivesDataDisplayVisitor());
    }

    private void handleStatusVozila() {
        System.out.printf("TRENUTNO VRIJEME: %s%n", TerminalCommandHandler.getInstance().getCroDateString());
        System.out.printf("%-15s | %-15s | %-20s | %-12s | %-12s %n", "STATUS", "UKUPNO KM", "ZAUZETOST(%)", "BROJ VOZNJI", "BROJ PAKETA");
        RepositoryManager.getINSTANCE().getVehicleRepository()
                .getAll()
                .forEach(vehicle -> vehicle.accept(new VehicleDataDisplayVisitor()));
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
        int statusIndex = commandArray.length - 1;
        return commandArray[statusIndex];
    }

    private String getPackageId(String[] commandArray) {
        int packageIndex = commandArray.length - 2;
        return commandArray[packageIndex];
    }

    private String getPersonName(String command) {
        final Matcher matcher = patternPo.matcher(command);
        if (matcher.matches()) return matcher.group(1);
        return null;
    }

    private void updateObserver(String statusUpdate, Paket paket, Observer observer) {
        if (statusUpdate.equals("D")) {
            paket.registerObserver(observer);
        } else {
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
        System.out.printf("%-5s | %-10s | %-10s | %-12s | %-12s | %-25s | %-20s | %-20s%n", "VRSTA", "USLUGA", "OZNAKA", "DOSTAVA", "POUZECE (V)", "STATUS", "VRIJEME PRIJEMA", "VRIJEME PREUZIMANJA");
        List<Paket> all = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        List<Paket> primljeniPaketi = all.stream().filter(paket -> paket.isReceived() && !paket.isDelivered()).toList();
        List<Paket> dostavljeniPaketi = all.stream().filter(Paket::isDelivered).toList();
        primljeniPaketi.forEach(Paket::soutPackage);
        System.out.println("\nDostavljeni");
        System.out.printf("%-5s | %-10s | %-10s | %-12s | %-12s | %-25s | %-20s | %-20s%n", "VRSTA", "USLUGA", "OZNAKA", "DOSTAVA", "POUZECE", "STATUS", "VRIJEME PRIJEMA", "VRIJEME PREUZIMANJA");

        dostavljeniPaketi.forEach(Paket::soutPackage);
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
