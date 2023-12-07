package org.foi.uzdiz.mmusica.visitor;

import org.foi.uzdiz.mmusica.model.Vehicle;

public class VehicleDataDisplayVisitor implements DataDisplayVisitor{
    @Override
    public void visitVehicle(Vehicle vehicle) {
        String s = vehicle.getVehicleState().giveStatus();
        String ukupnoKm = "TRI";
        int brojPaketa= vehicle.getPackages().size();
        String postotakZauzeca = "TRI";
        String brojVoznji = "TRI";
        System.out.printf("%s\t%s\t%10o\t%20s\t%10s%n", s,ukupnoKm,brojPaketa,postotakZauzeca,brojVoznji);
    }
}
