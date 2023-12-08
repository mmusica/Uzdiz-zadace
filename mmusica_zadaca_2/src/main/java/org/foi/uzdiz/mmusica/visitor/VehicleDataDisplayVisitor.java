package org.foi.uzdiz.mmusica.visitor;

import org.foi.uzdiz.mmusica.model.Vehicle;

public class VehicleDataDisplayVisitor implements DataDisplayVisitor{
    @Override
    public void visitVehicle(Vehicle vehicle) {

        String status = vehicle.getVehicleState().giveStatus();
        String ukupnoKm = "notImplemented";
        String postotakZauzeca = "M3: "+ round(vehicle.getLoadedSpacePercentage()*100) + " KG: "+round(vehicle.getLoadedWeightPercentage()*100);
        String brojVoznji = ""+vehicle.getBrojVoznji();
        System.out.printf("%-15s | %-15s | %-20s | %-12s | %-12s -> %s %s %n",status, ukupnoKm, postotakZauzeca, brojVoznji, vehicle.getUkupanBrojPaketaPoVrstiString(),vehicle.getOpis(), vehicle.getRegistracija());

    }

    private double round(double a){
        return Math.round(a * 100.0) / 100.0;
    }
}

