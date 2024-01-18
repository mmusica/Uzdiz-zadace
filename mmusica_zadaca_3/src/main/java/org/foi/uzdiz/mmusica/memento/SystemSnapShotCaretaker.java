package org.foi.uzdiz.mmusica.memento;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemSnapShotCaretaker {
    public static final String OVAKAV_SNAPSHOT_NE_POSTOJI = "Ovakav snapshot ne postoji";
    private final Map<String, Map<String, Paket.PaketMemento>> paketMementoMap = new HashMap<>();
    private final Map<String, Map<String, Vehicle.VehicleMemento>> vehicleMementoMap = new HashMap<>();
    private final Map<String, TerminalCommandHandler.TerminalCommandHandlerMemento> vr = new HashMap<>();
    private static final SystemSnapShotCaretaker INSTANCE;

    static {
        INSTANCE = new SystemSnapShotCaretaker();
    }

    private SystemSnapShotCaretaker() {
    }

    public static SystemSnapShotCaretaker getInstance() {
        return INSTANCE;
    }

    public void takeCurrentSystemSnapshot(String saveWord, List<Paket> paketi, List<Vehicle> vehicles) {
        for (Paket paket : paketi) {
            snapShotPaket(paket, saveWord);
        }
        for (Vehicle vehicle : vehicles) {
            snapShotVehicle(vehicle, saveWord);
        }
        snapShotTerminalCommandHandler(saveWord);
    }

    public void restoreCurrentSystem(String saveWord, List<Paket> paketi, List<Vehicle> vehicles) {
        for (Paket paket : paketi) {
            restorePaket(paket, saveWord);
        }
        for (Vehicle vehicle : vehicles) {
            restoreVehicle(vehicle, saveWord);
        }
        restoreTerminalCommandHandler(saveWord);
        //Sada kad smo vratili nove refrence objektu - ovi saveovi na saveWordu su koristeni pa ako zelimo opet iskoristiti
        //taj save onda trebamo kreirati novi save
        //kreiramo identican save ko onome kaj smo sad iskoristili i zamijenjujemo ga u mapi
        takeCurrentSystemSnapshot(saveWord, paketi, vehicles);
    }

    private void snapShotPaket(Paket paket, String saveWord) {
        if (!paketMementoMap.containsKey(saveWord)) {
            paketMementoMap.put(saveWord, new HashMap<>());
        }
        Map<String, Paket.PaketMemento> paketPaketMementoMap = paketMementoMap.get(saveWord);
        paketPaketMementoMap.put(paket.getOznaka(), paket.takeSnapshot());
    }

    private void snapShotVehicle(Vehicle vehicle, String saveWord) {
        if (!vehicleMementoMap.containsKey(saveWord)) {
            vehicleMementoMap.put(saveWord, new HashMap<>());
        }
        Map<String, Vehicle.VehicleMemento> vehicleVehicleMementoMap = vehicleMementoMap.get(saveWord);
        vehicleVehicleMementoMap.put(vehicle.getRegistracija(), vehicle.takeSnapshot());
    }

    private void snapShotTerminalCommandHandler(String saveWord) {
        vr.put(saveWord, TerminalCommandHandler.getInstance().takeSnapshot());
    }

    private void restorePaket(Paket paket, String saveWord) {
        if (paketMementoMap.containsKey(saveWord)) {
            Map<String, Paket.PaketMemento> paketPaketMementoMap = paketMementoMap.get(saveWord);
            Paket.PaketMemento paketMemento = paketPaketMementoMap.get(paket.getOznaka());
            paket.restore(paketMemento);
        } else {
            System.out.println(OVAKAV_SNAPSHOT_NE_POSTOJI);
        }
    }

    private void restoreVehicle(Vehicle vehicle, String saveWord) {
        if (vehicleMementoMap.containsKey(saveWord)) {
            Map<String, Vehicle.VehicleMemento> vehicleVehicleMementoMap = vehicleMementoMap.get(saveWord);
            Vehicle.VehicleMemento vehicleMemento = vehicleVehicleMementoMap.get(vehicle.getRegistracija());
            vehicle.restore(vehicleMemento);
        } else {
            System.out.println(OVAKAV_SNAPSHOT_NE_POSTOJI);
        }
    }

    private void restoreTerminalCommandHandler(String saveWord) {
        if (vr.containsKey(saveWord)) {
            TerminalCommandHandler.getInstance().restore(vr.get(saveWord));
        } else {
            System.out.println(OVAKAV_SNAPSHOT_NE_POSTOJI);
        }
    }
}

