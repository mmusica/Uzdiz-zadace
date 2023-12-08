package org.foi.uzdiz.mmusica.visitor;

import org.foi.uzdiz.mmusica.model.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DrivesDataDisplayVisitor implements DataDisplayVisitor{
    @Override
    public void visitVehicle(Vehicle vehicle) {
        var brojPaketa = vehicle.getUkupanBrojPaketaPoVrstiString();
        var ukupnoKm = "notImplemented";
        var vrijemePoc = vehicle.getDeliveryStarted();
        var vrijemePovr = vehicle.getVrijemePovratka();

        Duration trajanje;
        String trajanjeString = "----";
        if(vrijemePoc == null){
            System.out.println("Vozilo jos nema voznje");
            return;
        }
        if(vrijemePovr != null){
            trajanje = Duration.between(vrijemePoc, vrijemePovr);
            trajanjeString = getTrajanjeString(trajanje);
            System.out.printf("%-25s | %-25s | %-12s | %-20s | %-12s%n", getCroatianDate(vrijemePoc), getCroatianDate(vrijemePovr), trajanjeString,
                    ukupnoKm, brojPaketa);
        }else{
            System.out.printf("%-25s | %-25s | %-12s | %-20s | %-12s%n", getCroatianDate(vrijemePoc), "----", trajanjeString,
                    ukupnoKm, brojPaketa);
        }


    }

    private static String getTrajanjeString(Duration trajanje) {
        long trajanjeSeconds =  trajanje.toSeconds();
        long trajanjeMinutes =  trajanje.toMinutes();
        if(trajanje.toSeconds()>=60){
             trajanjeSeconds = trajanje.toMinutes() * 60 - trajanje.toSeconds();
        }
        if(trajanje.toMinutes()>=60){
            trajanjeMinutes = trajanje.toHours() * 60 - trajanje.toMinutes();
        }
        return trajanje.toHours() + "H:" + trajanjeMinutes + "M:" + trajanjeSeconds + "S";
    }

    public String getCroatianDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return dateTime.format(formatter);
    }
}
