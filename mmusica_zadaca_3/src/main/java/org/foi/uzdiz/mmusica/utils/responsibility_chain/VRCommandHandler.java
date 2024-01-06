package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.office.ReceptionOffice;

public class VRCommandHandler extends UserCommandHandler {
    private final ReceptionOffice receptionOffice = new ReceptionOffice();
    public VRCommandHandler(UserCommandHandler next) {
        super(next);
    }
    @Override
    public void executeCommand(String[] commandArray) {
        if (isVirtualnoVrijemeValid(commandArray)) {
            receptionOffice.receivePackage(commandArray[1]);
        }
    }
    @Override
    String getCommand() {
        return "VR";
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
