package org.foi.uzdiz.mmusica.memento;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Vehicle;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemSnapShotCaretaker {
    private final Map<String, Map<Paket, Paket.PaketMemento>> paketMementoMap = new HashMap<>();
    private final Map<String, Map<Vehicle, Vehicle.VehicleMemento>> vehicleMementoMap = new HashMap<>();
    private final Map<String, TerminalCommandHandler.TerminalCommandHandlerMemento> vr = new HashMap<>();
    private static final SystemSnapShotCaretaker INSTANCE;

    static{
        INSTANCE = new SystemSnapShotCaretaker();
    }
    private SystemSnapShotCaretaker() {
    }

    public static SystemSnapShotCaretaker getInstance(){
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
    }

    private void snapShotPaket(Paket paket, String saveWord) {
        if (paketMementoMap.containsKey(saveWord)) {
            Map<Paket, Paket.PaketMemento> paketPaketMementoMap = paketMementoMap.get(saveWord);
            paketPaketMementoMap.put(paket, paket.takeSnapshot());
        } else {
            paketMementoMap.put(saveWord, new HashMap<>());
        }
    }

    private void snapShotVehicle(Vehicle vehicle, String saveWord) {
        if (vehicleMementoMap.containsKey(saveWord)) {
            Map<Vehicle, Vehicle.VehicleMemento> vehicleVehicleMementoMap = vehicleMementoMap.get(saveWord);
            vehicleVehicleMementoMap.put(vehicle, vehicle.takeSnapshot());
        } else {
            vehicleMementoMap.put(saveWord, new HashMap<>());
        }
    }

    private void snapShotTerminalCommandHandler(String saveWord) {
        vr.put(saveWord, TerminalCommandHandler.getInstance().takeSnapshot());
    }

    private void restorePaket(Paket paket, String saveWord) {
        if (paketMementoMap.containsKey(saveWord)) {
            Map<Paket, Paket.PaketMemento> paketPaketMementoMap = paketMementoMap.get(saveWord);
            Paket.PaketMemento paketMemento = paketPaketMementoMap.get(paket);
            paket.restore(paketMemento);
        } else {
            System.out.println("Ovakav snapshot ne postoji");
        }
    }

    private void restoreVehicle(Vehicle vehicle, String saveWord) {
        if (vehicleMementoMap.containsKey(saveWord)) {
            Map<Vehicle, Vehicle.VehicleMemento> vehicleVehicleMementoMap = vehicleMementoMap.get(saveWord);
            Vehicle.VehicleMemento vehicleMemento = vehicleVehicleMementoMap.get(vehicle);
            vehicle.restore(vehicleMemento);
        } else {
            System.out.println("Ovakav snapshot ne postoji");
        }
    }

    private void restoreTerminalCommandHandler(String saveWord) {
        if (vr.containsKey(saveWord)) {
            TerminalCommandHandler.getInstance().restore(vr.get(saveWord));
        } else {
            System.out.println("Ovakav snapshot ne postoji");
        }
    }
}

