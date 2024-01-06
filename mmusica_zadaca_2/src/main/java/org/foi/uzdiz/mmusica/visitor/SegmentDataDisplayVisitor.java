package org.foi.uzdiz.mmusica.visitor;

import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.voznja.Drive;
import org.foi.uzdiz.mmusica.voznja.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SegmentDataDisplayVisitor implements DataDisplayVisitor {

    private int index;

    public SegmentDataDisplayVisitor(int index) {
        this.index = index - 1;
    }

    @Override
    public void visitVehicle(Vehicle vehicle) {

        if (vehicle.getDrives().isEmpty()) {
            System.out.println("Vozilo nema voznje");
            return;
        }
        Drive drive = null;
        try {
             drive = vehicle.getDrives().get(index);
        }catch (Exception exception){
            System.out.println("Ovakva voznja ne postoji");
            return;
        }

        for (Segment segment : drive.getSegments()) {
            var ukupnoKm = segment.getDistance();
            LocalDateTime vrijemePoc = segment.getStartTime();
            LocalDateTime vrijemePovr = segment.getFinishTime();
            String trajanjeString = null;
            String vrijemePovrStr;
            Duration between = Duration.between(vrijemePoc, vrijemePovr);
            trajanjeString = String.valueOf(between.getSeconds()) + " sec";
            vrijemePovrStr = getCroatianDate(vrijemePovr);

            String oznakaPaketa = "null";
            if(segment.getPaket()!=null) oznakaPaketa = segment.getPaket().getOznaka();

            System.out.printf("%-25s | %-25s | %-20s | %-20s | %-12s%n", getCroatianDate(vrijemePoc), vrijemePovrStr, trajanjeString,
                    ukupnoKm, oznakaPaketa);
            System.out.println("------------------------------------------------------------------------------------------------------------------");
        }

    }

    public String getCroatianDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return dateTime.format(formatter);
    }
}

