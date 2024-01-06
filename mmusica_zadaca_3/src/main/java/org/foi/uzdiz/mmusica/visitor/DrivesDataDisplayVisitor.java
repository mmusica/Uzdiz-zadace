package org.foi.uzdiz.mmusica.visitor;

import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.voznja.Drive;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DrivesDataDisplayVisitor implements DataDisplayVisitor {
    @Override
    public void visitVehicle(Vehicle vehicle) {

        if(vehicle.getDrives().isEmpty()) {
            System.out.println("Vozilo nema voznje");
            return;
        }

        for (Drive drive : vehicle.getDrives()) {
            var brojPaketa = drive.getUkupanBrojPaketaPoVrstiString();
            var ukupnoKm = drive.getDistance();
            LocalDateTime vrijemePoc = drive.getStartTime();
            LocalDateTime vrijemePovr = drive.getReturnTime();
            String trajanjeString = null;
            String vrijemePovrStr;
            Duration between = Duration.between(vrijemePoc, drive.getFinishTimeOfLastSegment());
            trajanjeString = String.valueOf(between.getSeconds()) + " sec";
            if(vrijemePovr != null){
                vrijemePovrStr = getCroatianDate(vrijemePovr);
            }else{
                vrijemePovrStr = "----------";
            }
            System.out.printf("%-25s | %-25s | %-20s | %-20s | %-12s%n", getCroatianDate(vrijemePoc), vrijemePovrStr, trajanjeString,
                    ukupnoKm, brojPaketa);
            System.out.println("------------------------------------------------------------------------------------------------------------------");
        }

    }

    private static String getTrajanjeString(Duration trajanje) {
        long trajanjeSeconds = trajanje.toSeconds();
        long trajanjeMinutes = trajanje.toMinutes();
        if (trajanje.toSeconds() >= 60) {
            trajanjeSeconds = trajanje.toMinutes() * 60 - trajanje.toSeconds();
        }
        if (trajanje.toMinutes() >= 60) {
            trajanjeMinutes = trajanje.toHours() * 60 - trajanje.toMinutes();
        }
        return trajanje.toHours() + "H:" + trajanjeMinutes + "M:" + trajanjeSeconds + "S";
    }

    public String getCroatianDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return dateTime.format(formatter);
    }
}
