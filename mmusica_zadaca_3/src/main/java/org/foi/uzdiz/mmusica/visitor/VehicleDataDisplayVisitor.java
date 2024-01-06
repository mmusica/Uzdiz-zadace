package org.foi.uzdiz.mmusica.visitor;

import org.foi.uzdiz.mmusica.model.Vehicle;

import java.text.DecimalFormat;

public class VehicleDataDisplayVisitor implements DataDisplayVisitor{
    @Override
    public void visitVehicle(Vehicle vehicle) {

        String status = vehicle.getVehicleState().giveStatus();

        double value = vehicle.getAllDrivesDistance() ;
        String pattern = "#.####";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String ukupnoKm = myFormatter.format(value);

        String postotakZauzeca = "M3: "+ round(vehicle.getLoadedSpacePercentage()*100) + " KG: "
                +round(vehicle.getLoadedWeightPercentage()*100);
        String brojVoznji = ""+ (long) vehicle.getDrives().size();
        System.out.printf("%-15s | %-15s | %-20s | %-12s | %-12s -> %s %s %n",status,
                ukupnoKm, postotakZauzeca, brojVoznji, vehicle.getUkupanBrojPaketaPoVrstiString(),vehicle.getOpis(),
                vehicle.getRegistracija());

    }


    private double round(double a){
        return Math.round(a * 100.0) / 100.0;
    }
}

