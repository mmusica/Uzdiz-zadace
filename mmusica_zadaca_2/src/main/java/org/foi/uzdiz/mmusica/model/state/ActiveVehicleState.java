package org.foi.uzdiz.mmusica.model.state;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class ActiveVehicleState implements VehicleState {
    private final Vehicle vehicle;
    //private final DeliveryStrategy deliveryStrategy;
    public ActiveVehicleState(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public void finalizeDeliveries() {
        LocalDateTime currentTime = TerminalCommandHandler.getInstance().getVirtualniSat();
        //Sada tu osim kaj zavrsi dostavu, moramo jos utvrditi kolko mu treba da se vrati do ureda i tek onda bude gotov
        //za svaki paket ponovno settat deliveryFinishedBy i ne settat svaki paket u listi nego jedan po jedan paket
        if (this.vehicle.isDriving() && (this.vehicle.getDeliveryFinishedBy().isEqual(currentTime) || this.vehicle.getDeliveryFinishedBy().isBefore(currentTime))) {
            System.out.printf("ZAVRSENA DOSTAVA vozila %s u virtualno vrijeme: %s%n",
                    this.vehicle.getOpis(), TerminalCommandHandler.getInstance().getCroDateString());
            this.vehicle.setVrijemePovratka(TerminalCommandHandler.getInstance().getVirtualniSat().plusSeconds(0));
            //
            int i1 = vehicle.getBrojVoznji() + 1;
            vehicle.setBrojVoznji(i1);
            AtomicInteger i = new AtomicInteger(vehicle.getBrojIsporucenih());
            //
            this.vehicle.getPackages().forEach(paket -> {
                paket.setBeingDelivered(false);
                paket.setDelivered(true);
                this.vehicle.setMoney(this.vehicle.getMoney().add(paket.getVehiclePrice()));
                paket.setStatusIsporuke("Dostavljen");
                //
                i.getAndIncrement();
            });
            this.vehicle.setBrojIsporucenih(i.get());
            this.clearData();
        }
    }
    @Override
    public Paket loadPackageIntoVehicle(Paket paket) {
        if (hasEnoughCapacity(vehicle, paket) && hasEnoughWeight(vehicle, paket) && !paket.isBeingDelivered() && !paket.isErrored()) {
            this.vehicle.setGetCurrentlyLoadedCapacity(this.vehicle.getGetCurrentlyLoadedCapacity() + paket.calculatePackageSize());
            this.vehicle.setCurrentlyLoadedWeight(this.vehicle.getCurrentlyLoadedWeight() + paket.getTezina());

            paket.setBeingDelivered(true);
            System.out.printf("VRIJEME %s: Ukrcan paket s oznakom %s hitnosti %s na vozilo %s%n", TerminalCommandHandler.getInstance().getCroDateString(), paket.getOznaka(), paket.getUslugaDostave(), vehicle.getOpis());
            paket.setStatusIsporuke("Ukrcan u vozilo");

            this.vehicle.getPackages().add(paket);
            return this.vehicle.getPackages().get(vehicle.getPackages().size()-1);
        }
        return null;
    }
    private boolean hasEnoughCapacity(Vehicle vehicle, Paket paket) {
        double capacity = vehicle.getKapacitetProstora() - vehicle.getGetCurrentlyLoadedCapacity();
        double packageSize = paket.calculatePackageSize();
        return packageSize <= capacity;
    }
    private boolean hasEnoughWeight(Vehicle vehicle, Paket paket) {
        double capacity = vehicle.getKapacitetTezine() - vehicle.getCurrentlyLoadedWeight();
        double packageWeight = paket.getTezina();
        return packageWeight <= capacity;
    }
    @Override
    public void startDeliveringPackages() {
        this.vehicle.setDriving(true);
        //
        var trenutnoVrijeme = TerminalCommandHandler.getInstance().getVirtualniSat().plusSeconds(0);
        this.vehicle.setDeliveryStarted(trenutnoVrijeme);
        this.vehicle.setVrijemePovratka(null);
        //
        this.vehicle.setDeliveryFinishedBy(TerminalCommandHandler.getInstance().getVirtualniSat()
                .plusMinutes(TerminalCommandHandler.getInstance().getVrijemeIsporuke()));
        System.out.printf("Vozilo %s krenulo u isporuku i biti ce gotovo nakon %s%n", this.vehicle.getOpis(),
                vehicle.getCroatianDate(this.vehicle.getDeliveryFinishedBy()));
        this.vehicle.getPackages().forEach(paket -> {
            paket.setBeingDelivered(true);
            paket.setStatusIsporuke("Trenutno u isporuci");
        });

    }

    @Override
    public void clearData() {
        this.vehicle.getPackages().clear();
        this.vehicle.setCurrentlyLoadedWeight(0);
        this.vehicle.setGetCurrentlyLoadedCapacity(0);
        this.vehicle.setDriving(false);
        this.vehicle.setDeliveryFinishedBy(null);
    }

    @Override
    public String giveStatus() {
        return "Aktivan";
    }
}