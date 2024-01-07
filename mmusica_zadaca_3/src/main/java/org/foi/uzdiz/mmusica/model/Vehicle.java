package org.foi.uzdiz.mmusica.model;

import org.foi.uzdiz.mmusica.enums.TypeOfService;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.model.state.BrokenVehicleState;
import org.foi.uzdiz.mmusica.model.state.VehicleContext;
import org.foi.uzdiz.mmusica.model.state.VehicleState;
import org.foi.uzdiz.mmusica.visitor.DataDisplayVisitor;
import org.foi.uzdiz.mmusica.visitor.VehicleDisplay;
import org.foi.uzdiz.mmusica.voznja.Drive;
import org.foi.uzdiz.mmusica.voznja.GPS;
import org.foi.uzdiz.mmusica.voznja.Segment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vehicle implements VehicleContext, VehicleDisplay {
    private String registracija;
    private String opis;
    private double kapacitetTezine;
    private double kapacitetProstora;
    private int redoslijed;
    private float prosjecnaBrzina;
    private List<Location> deliveryArea;
    private BigDecimal money;
    private List<Paket> packages;
    private boolean isDriving;
    private LocalDateTime deliveryFinishedBy;
    private double currentlyLoadedWeight;
    private double currentlyLoadedCapacity;
    private VehicleState vehicleState;
    private GPS currentGPS;
    private int brojIsporucenih;
    private List<Drive> drives;

    public Vehicle(String registracija, String opis, double kapacitetTezine,
                   double kapacitetProstora, int redoslijed, BigDecimal money, List<Paket> packages,
                   float prosjecnaBrzina, List<Location> deliveryArea, GPS currentGPS) {
        this.registracija = registracija;
        this.opis = opis;
        this.kapacitetTezine = kapacitetTezine;
        this.kapacitetProstora = kapacitetProstora;
        this.redoslijed = redoslijed;
        this.money = money;
        this.packages = packages;
        this.currentlyLoadedCapacity = 0;
        this.currentlyLoadedWeight = 0;
        this.prosjecnaBrzina = prosjecnaBrzina;
        this.deliveryArea = deliveryArea;
        this.currentGPS = currentGPS;
        this.brojIsporucenih = 0;
        this.drives = new ArrayList<>();
    }

    public String getUkupanBrojPaketaPoVrstiString() {
        StringBuilder stringBuilder = new StringBuilder();
        int brojacHitni = 0;
        int brojacObicni = 0;
        for (Paket p : this.getPackages()) {
            if (p.getUslugaDostave().equals(TypeOfService.H.toString())) {
                brojacHitni++;
            } else {
                brojacObicni++;
            }
        }
        stringBuilder.append(brojacHitni).append(" HITNIH, ").append(brojacObicni).append(" NORMALNIH, ").append(this.brojIsporucenih).append(" DOSTAVLJENIH");
        return stringBuilder.toString();
    }

    @Override
    public void changeState(VehicleState vehicleState) {
        if (this.vehicleState instanceof BrokenVehicleState) {
            System.out.println("Neispravno vozilo ne moze mijenjati stanje!");
            return;
        }
        this.vehicleState = vehicleState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(getRegistracija(), vehicle.getRegistracija());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistracija());
    }

    public void clearData() {
        this.vehicleState.clearData();
    }

    public void finalizeDeliveries() {
        this.vehicleState.finalizeDeliveries();
    }

    public Paket loadPackageIntoVehicle(Paket paket) {
        return this.vehicleState.loadPackageIntoVehicle(paket);
    }

    public int getBrojIsporucenih() {
        return brojIsporucenih;
    }

    public void startDeliveringPackages() {
        this.vehicleState.startDeliveringPackages();
    }

    public double getLoadedWeightPercentage() {
        return currentlyLoadedWeight / kapacitetTezine;
    }

    public double getLoadedSpacePercentage() {
        return getCurrentlyLoadedCapacity() / kapacitetProstora;
    }

    public void setBrojIsporucenih(int brojIsporucenih) {
        this.brojIsporucenih = brojIsporucenih;
    }

    public String getCroatianDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return dateTime.format(formatter);
    }

    @Override
    public void accept(DataDisplayVisitor dataDisplayVisitor) {
        dataDisplayVisitor.visitVehicle(this);
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getRegistracija() {
        return registracija;
    }

    public void setRegistracija(String registracija) {
        this.registracija = registracija;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public double getKapacitetTezine() {
        return kapacitetTezine;
    }

    public void setKapacitetTezine(double kapacitetTezine) {
        this.kapacitetTezine = kapacitetTezine;
    }

    public double getKapacitetProstora() {
        return kapacitetProstora;
    }

    public void setKapacitetProstora(double kapacitetProstora) {
        this.kapacitetProstora = kapacitetProstora;
    }

    public int getRedoslijed() {
        return redoslijed;
    }

    public void setRedoslijed(int redoslijed) {
        this.redoslijed = redoslijed;
    }

    public List<Paket> getPackages() {
        return packages;
    }

    public void setPackages(List<Paket> packages) {
        this.packages = packages;
    }

    public boolean isDriving() {
        return isDriving;
    }

    public void setDriving(boolean driving) {
        isDriving = driving;
    }

    public LocalDateTime getDeliveryFinishedBy() {
        return deliveryFinishedBy;
    }

    public void setDeliveryFinishedBy(LocalDateTime deliveryFinishedBy) {
        this.deliveryFinishedBy = deliveryFinishedBy;
    }

    public double getCurrentlyLoadedWeight() {
        return currentlyLoadedWeight;
    }

    public void setCurrentlyLoadedWeight(double currentlyLoadedWeight) {
        this.currentlyLoadedWeight = currentlyLoadedWeight;
    }

    public double getCurrentlyLoadedCapacity() {
        return currentlyLoadedCapacity;
    }

    public void setCurrentlyLoadedCapacity(double getCurrentlyLoadedCapacity) {
        this.currentlyLoadedCapacity = getCurrentlyLoadedCapacity;
    }

    public float getProsjecnaBrzina() {
        return prosjecnaBrzina;
    }

    public void setProsjecnaBrzina(float prosjecnaBrzina) {
        this.prosjecnaBrzina = prosjecnaBrzina;
    }

    public List<Location> getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(List<Location> deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public VehicleState getVehicleState() {
        return vehicleState;
    }

    public GPS getCurrentGPS() {
        return currentGPS;
    }

    public void setCurrentGPS(GPS currentGPS) {
        this.currentGPS = currentGPS;
    }

    public List<Drive> getDrives() {
        return drives;
    }

    public void setDrives(List<Drive> drives) {
        this.drives = drives;
    }

    public double getAllDrivesDistance() {
        double totalDistance = 0;
        for (Drive drive : drives) {
            totalDistance += drive.getDistance();
        }
        return totalDistance;
    }

    public VehicleMemento takeSnapshot() {
        return new VehicleMemento(registracija, opis, kapacitetTezine, kapacitetProstora, redoslijed, prosjecnaBrzina, deliveryArea,
                money, packages, isDriving, deliveryFinishedBy, currentlyLoadedWeight, currentlyLoadedCapacity,
                vehicleState, currentGPS, brojIsporucenih, drives);
    }

    public void restore(VehicleMemento memento) {
        registracija = memento.getRegistracija();
        opis = memento.getOpis();
        kapacitetTezine = memento.getKapacitetTezine();
        kapacitetProstora = memento.getKapacitetProstora();
        redoslijed = memento.getRedoslijed();
        prosjecnaBrzina = memento.getProsjecnaBrzina();
        deliveryArea = memento.getDeliveryArea();
        money = memento.getMoney();
        packages = memento.getPackages();
        isDriving = memento.isDriving();
        deliveryFinishedBy = memento.getDeliveryFinishedBy();
        currentlyLoadedWeight = memento.getCurrentlyLoadedWeight();
        currentlyLoadedCapacity = memento.getCurrentlyLoadedCapacity();
        vehicleState = memento.getVehicleState();
        currentGPS = memento.getCurrentGPS();
        brojIsporucenih = memento.getBrojIsporucenih();
        drives = memento.getDrives();
    }

    public static class VehicleMemento {
        private final String registracija;
        private final String opis;
        private final double kapacitetTezine;
        private final double kapacitetProstora;
        private final int redoslijed;
        private final float prosjecnaBrzina;
        private final List<Location> deliveryArea;
        private final BigDecimal money;
        private final List<Paket> packages;
        private final boolean isDriving;
        private final LocalDateTime deliveryFinishedBy;
        private final double currentlyLoadedWeight;
        private final double currentlyLoadedCapacity;
        private final VehicleState vehicleState;
        private final GPS currentGPS;
        private final int brojIsporucenih;
        private final List<Drive> drives;

        public VehicleMemento(String registracija, String opis, double kapacitetTezine, double kapacitetProstora,
                              int redoslijed, float prosjecnaBrzina, List<Location> deliveryArea, BigDecimal money,
                              List<Paket> packages, boolean isDriving, LocalDateTime deliveryFinishedBy,
                              double currentlyLoadedWeight, double currentlyLoadedCapacity, VehicleState vehicleState,
                              GPS currentGPS, int brojIsporucenih, List<Drive> drives) {

            this.registracija = registracija;
            this.opis = opis;
            this.kapacitetTezine = kapacitetTezine;
            this.kapacitetProstora = kapacitetProstora;
            this.redoslijed = redoslijed;
            this.prosjecnaBrzina = prosjecnaBrzina;
            this.deliveryArea = new ArrayList<>(deliveryArea);
            this.money = money;
            this.packages = new ArrayList<>(packages);
            this.isDriving = isDriving;
            this.deliveryFinishedBy = deliveryFinishedBy;
            this.currentlyLoadedWeight = currentlyLoadedWeight;
            this.currentlyLoadedCapacity = currentlyLoadedCapacity;
            this.vehicleState = vehicleState;
            this.currentGPS = new GPS(currentGPS.getLat(),currentGPS.getLon());
            this.brojIsporucenih = brojIsporucenih;

            this.drives = new ArrayList<>();
            for (Drive d : drives){
                this.drives.add(d.clone());
            }
        }

        private String getRegistracija() {
            return registracija;
        }

        private String getOpis() {
            return opis;
        }

        private double getKapacitetTezine() {
            return kapacitetTezine;
        }

        private double getKapacitetProstora() {
            return kapacitetProstora;
        }

        private int getRedoslijed() {
            return redoslijed;
        }

        private float getProsjecnaBrzina() {
            return prosjecnaBrzina;
        }

        private List<Location> getDeliveryArea() {
            return deliveryArea;
        }

        private BigDecimal getMoney() {
            return money;
        }

        private List<Paket> getPackages() {
            return packages;
        }

        private boolean isDriving() {
            return isDriving;
        }

        private LocalDateTime getDeliveryFinishedBy() {
            return deliveryFinishedBy;
        }

        private double getCurrentlyLoadedWeight() {
            return currentlyLoadedWeight;
        }

        private double getCurrentlyLoadedCapacity() {
            return currentlyLoadedCapacity;
        }

        private VehicleState getVehicleState() {
            return vehicleState;
        }

        private GPS getCurrentGPS() {
            return currentGPS;
        }

        private int getBrojIsporucenih() {
            return brojIsporucenih;
        }

        private List<Drive> getDrives() {
            return drives;
        }
    }
}
