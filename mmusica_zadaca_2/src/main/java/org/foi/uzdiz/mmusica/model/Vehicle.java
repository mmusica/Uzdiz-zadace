package org.foi.uzdiz.mmusica.model;

import org.foi.uzdiz.mmusica.model.locations.Location;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Vehicle {
    private String registracija;
    private String opis;
    private double kapacitetTezine;
    private double kapacitetProstora;
    private int redoslijed;
    private float prosjecnaBrzina;

    private List<Location> deliveryArea;
    private String status;
    private BigDecimal money;
    private List<Paket> packages;
    private boolean isDriving;
    private LocalDateTime deliveryFinishedBy;
    private double currentlyLoadedWeight;
    private double getCurrentlyLoadedCapacity;

    public Vehicle(String registracija, String opis, double kapacitetTezine,
                   double kapacitetProstora, int redoslijed, BigDecimal money, List<Paket> packages,
                   float prosjecnaBrzina, List<Location> deliveryArea, String status) {
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
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
