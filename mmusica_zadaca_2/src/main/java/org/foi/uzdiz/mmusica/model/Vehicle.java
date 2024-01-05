package org.foi.uzdiz.mmusica.model;

import org.foi.uzdiz.mmusica.enums.TypeOfService;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.model.state.BrokenVehicleState;
import org.foi.uzdiz.mmusica.model.state.VehicleContext;
import org.foi.uzdiz.mmusica.model.state.VehicleState;
import org.foi.uzdiz.mmusica.visitor.DataDisplayVisitor;
import org.foi.uzdiz.mmusica.visitor.VehicleDisplay;
import org.foi.uzdiz.mmusica.voznja.GPS;
import org.foi.uzdiz.mmusica.voznja.Drive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDateTime deliveryStarted;
    private LocalDateTime vrijemePovratka;
    private LocalDateTime deliveryFinishedBy;
    private double currentlyLoadedWeight;
    private double getCurrentlyLoadedCapacity;
    private VehicleState vehicleState;
    private GPS currentGPS;
    //
    private int brojIsporucenih;
    private int brojVoznji;
    //
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
        this.getCurrentlyLoadedCapacity = 0;
        this.currentlyLoadedWeight = 0;
        this.prosjecnaBrzina = prosjecnaBrzina;
        this.deliveryArea = deliveryArea;
        this.currentGPS = currentGPS;
        this.brojVoznji = 0;
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

    public int getBrojVoznji() {
        return brojVoznji;
    }

    public double getLoadedWeightPercentage() {
        return currentlyLoadedWeight / kapacitetTezine;
    }

    public double getLoadedSpacePercentage() {
        return getGetCurrentlyLoadedCapacity() / kapacitetProstora;
    }

    public void setBrojVoznji(int brojVoznji) {
        this.brojVoznji = brojVoznji;
    }

    public void setBrojIsporucenih(int brojIsporucenih) {
        this.brojIsporucenih = brojIsporucenih;
    }

    public void startDeliveringPackages() {
        this.vehicleState.startDeliveringPackages();
    }

    public String getCroatianDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return dateTime.format(formatter);
    }

    @Override
    public void accept(DataDisplayVisitor dataDisplayVisitor) {
        dataDisplayVisitor.visitVehicle(this);
    }

    public LocalDateTime getDeliveryStarted() {
        return deliveryStarted;
    }

    public void setDeliveryStarted(LocalDateTime deliveryStarted) {
        this.deliveryStarted = deliveryStarted;
    }

    public LocalDateTime getVrijemePovratka() {
        return vrijemePovratka;
    }

    public void setVrijemePovratka(LocalDateTime vrijemePovratka) {
        this.vrijemePovratka = vrijemePovratka;
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

    public double getGetCurrentlyLoadedCapacity() {
        return getCurrentlyLoadedCapacity;
    }

    public void setGetCurrentlyLoadedCapacity(double getCurrentlyLoadedCapacity) {
        this.getCurrentlyLoadedCapacity = getCurrentlyLoadedCapacity;
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

    public void setVehicleState(VehicleState vehicleState) {
        this.vehicleState = vehicleState;
    }

    public List<Drive> getDrives() {
        return drives;
    }

    public void setDrives(List<Drive> drives) {
        this.drives = drives;
    }
}
