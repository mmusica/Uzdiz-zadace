package org.foi.uzdiz.mmusica.office;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceptionOffice {
    private final Repository<Paket> paketRepository = RepositoryManager.getINSTANCE().getPackageRepository();
    private final DeliveryOffice deliveryOffice = new DeliveryOffice();
    private List<Paket> receivedPackages = new ArrayList<>();

    public BigDecimal money = new BigDecimal(0);
    public void receivePackage(String timeToIncrement) {
        LocalDateTime originalTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        LocalTime nextHour = TerminalCommandHandler.getInstance().getVirtualniSat().plusHours(1).toLocalTime();
        while (true) {

            deliveryOffice.deliverPackages();
            if(isFullHourAndNotOriginaltime(TerminalCommandHandler.getInstance().getVirtualniSat(), originalTime, nextHour)){
                nextHour = TerminalCommandHandler.getInstance().getVirtualniSat().plusHours(1).toLocalTime();

                receivedPackages.addAll(getReceivedPackages());
                receivedPackages = deliveryOffice.loadPackages(receivedPackages);
                deliveryOffice.deliverPackages();
            }


            if (isDone(timeToIncrement, originalTime)) break;
            Logger.getGlobal().log(Level.INFO, "ISPIS VIRTUALNOG SATA: " + TerminalCommandHandler.getInstance().getCroDateString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            incrementVirtualniSat(TerminalCommandHandler.getInstance().getMnoziteljSekunde());

        }
    }

    private boolean isFullHourAndNotOriginaltime(LocalDateTime virtualniSat, LocalDateTime originalTime, LocalTime nextHour) {
        return virtualniSat.getHour() >= nextHour.getHour() && !virtualniSat.isEqual(originalTime);
    }

    private List<Paket> getReceivedPackages() {
        LocalDateTime currentVirtualniSat = TerminalCommandHandler.getInstance().getVirtualniSat();
        List<Paket> receivedPackages = new ArrayList<>();
        paketRepository.getAll().forEach(paket -> {
            if ((paket.getTimeOfReceival().isBefore(currentVirtualniSat) || paket.getTimeOfReceival().isEqual(currentVirtualniSat)) && !paket.isReceived()) {
                paket.setReceived(true);
                System.out.println("Paket " + paket.getOznaka() + " -> PREUZET");
                paket.setStatusIsporuke("Preuzet");
                paket.setVrijemePreuzimanja(currentVirtualniSat);
                money = money.add(paket.calculatePrice());
                receivedPackages.add(paket);
            }
        });
        return receivedPackages;
    }

    private boolean isDone(String timeToIncrement, LocalDateTime originalTime) {
        if (!isWorkingTime()) {
            Logger.getGlobal().log(Level.INFO, "Vrijeme rada tvrtke je gotovo, vracam se na kraj radnog vremena, zavrsavam dan...");
            int hour = TerminalCommandHandler.getInstance().getPocetakRada().getHour();
            int minute = TerminalCommandHandler.getInstance().getPocetakRada().getMinute();
            if(TerminalCommandHandler.getInstance().getVirtualniSat().toLocalTime().isBefore(TerminalCommandHandler.getInstance().getPocetakRada())){
                LocalDateTime newWorkDay = TerminalCommandHandler.getInstance().getVirtualniSat().withHour(hour).withMinute(minute);
                TerminalCommandHandler.getInstance().setVirtualniSat(newWorkDay);
            }else{
                LocalDateTime newWorkDay = TerminalCommandHandler.getInstance().getVirtualniSat().plusDays(1).withHour(hour).withMinute(minute);
                TerminalCommandHandler.getInstance().setVirtualniSat(newWorkDay);
            }
            return true;
        }
        if (isIncrementedTime(timeToIncrement, originalTime)) {
            Logger.getGlobal().log(Level.INFO, "Dosao do zeljenog vremena unesenog VR komandom, zavrsavam rad...");
            return true;
        }
        return false;
    }

    private void incrementVirtualniSat(long mnoziteljSekunde) {
        LocalDateTime virtualniSat = TerminalCommandHandler.getInstance().getVirtualniSat();
        LocalDateTime updatedVirtualniSat = virtualniSat.plusSeconds(mnoziteljSekunde);
        TerminalCommandHandler.getInstance().setVirtualniSat(updatedVirtualniSat);
    }

    private boolean isWorkingTime() {
        LocalDateTime virtualniSat = TerminalCommandHandler.getInstance().getVirtualniSat();
        LocalTime pocetakRada = TerminalCommandHandler.getInstance().getPocetakRada();
        LocalTime krajRada = TerminalCommandHandler.getInstance().getKrajRada();
        LocalTime localTime = virtualniSat.toLocalTime();
        return localTime.isBefore(krajRada) && localTime.isAfter(pocetakRada) || localTime.equals(pocetakRada);
    }

    private boolean isIncrementedTime(String timeToIncrement, LocalDateTime originalTime) {
        Long hours = Long.decode(timeToIncrement);
        LocalDateTime endTimeWithIncrement = originalTime.plusHours(hours);
        if (TerminalCommandHandler.getInstance().getVirtualniSat().equals(endTimeWithIncrement)
                || TerminalCommandHandler.getInstance().getVirtualniSat().isAfter(endTimeWithIncrement)) {

            TerminalCommandHandler.getInstance().setVirtualniSat(endTimeWithIncrement);
            return true;
        }
        return false;
    }
}
