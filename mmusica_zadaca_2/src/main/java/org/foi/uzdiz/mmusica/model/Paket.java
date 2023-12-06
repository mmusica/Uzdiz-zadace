package org.foi.uzdiz.mmusica.model;

import org.foi.uzdiz.mmusica.enums.TypeOfPackage;
import org.foi.uzdiz.mmusica.enums.TypeOfService;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.observer.Observer;
import org.foi.uzdiz.mmusica.observer.Subject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Paket implements Subject {
    private List<Observer> observerList = new ArrayList<>();
    private String oznaka;
    private LocalDateTime vrijemePrijema;
    private  Person posiljatelj;
    private Person primatelj;
    private PackageType vrstaPaketa;
    private double visina;
    private double sirina;
    private double duzina;
    private double tezina;
    private String uslugaDostave;
    private BigDecimal iznosPouzeca;
    private String statusIsporuke;
    private LocalDateTime vrijemePreuzimanja;
    //Ovih 4 booleana mogu biti state
    private boolean isReceived;
    private boolean isBeingDelivered;
    private boolean isDelivered;
    private boolean isErrored;

    public Paket() {
    }
    @Override
    public void registerObserver(Observer observer) {
        if(!observerList.contains(observer)) this.observerList.add(observer);
    }

    public Location getPosiljateljLocation(String name){
        for (Observer observer : this.observerList) {

        }
        return null;
    }
    @Override
    public void removeObserver(Observer observer) {
        this.observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observerList.forEach(observer -> observer.update(this));
    }
    @Override
    public void setStatusIsporuke(String statusIsporuke) {
        this.statusIsporuke = statusIsporuke;
        this.notifyObservers();
    }

    @Override
    public String getStatus() {
        return this.oznaka + ": " + this.statusIsporuke;
    }

    public BigDecimal calculatePrice(){
        BigDecimal basePrice = new BigDecimal(0);
        if(uslugaDostave.equals(TypeOfService.P.toString())){
            return basePrice;
        }
        if(!uslugaDostave.equals(TypeOfService.H.toString())){
            basePrice = vrstaPaketa.getCijena();
        }else {
            basePrice = vrstaPaketa.getCijenaHitno();
        }
        if(vrstaPaketa.getOznaka().equals(TypeOfPackage.X.toString())){
            BigDecimal p = vrstaPaketa.getCijenaP().multiply(BigDecimal.valueOf(visina*sirina*duzina));
            BigDecimal t = vrstaPaketa.getCijenaT().multiply(BigDecimal.valueOf(tezina));
            basePrice = basePrice.add(p).add(t);

        }
        return basePrice;
    }
    public BigDecimal getVehiclePrice(){
        BigDecimal basePrice = new BigDecimal(0);
        if(uslugaDostave.equals(TypeOfService.P.toString())){
            basePrice = basePrice.add(iznosPouzeca);
        }
        return basePrice;
    }
    public LocalDateTime getTimeOfReceival() {
        return vrijemePrijema;
    }
    private String getCroatianDate(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return dateTime.format(formatter);
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public LocalDateTime getVrijemePrijema() {
        return vrijemePrijema;
    }

    public void setVrijemePrijema(LocalDateTime vrijemePrijema) {
        this.vrijemePrijema = vrijemePrijema;
    }

    public Person getPosiljatelj() {
        return posiljatelj;
    }

    public void setPosiljatelj(Person posiljatelj) {
        this.posiljatelj = posiljatelj;
    }

    public Person getPrimatelj() {
        return primatelj;
    }

    public void setPrimatelj(Person primatelj) {
        this.primatelj = primatelj;
    }

    public PackageType getVrstaPaketa() {
        return vrstaPaketa;
    }

    public void setVrstaPaketa(PackageType vrstaPaketa) {
        this.vrstaPaketa = vrstaPaketa;
    }

    public double getTezina() {
        return tezina;
    }

    public String getUslugaDostave() {
        return uslugaDostave;
    }

    public void setUslugaDostave(String uslugaDostave) {
        this.uslugaDostave = uslugaDostave;
    }

    public BigDecimal getIznosPouzeca() {
        return iznosPouzeca;
    }

    public void setIznosPouzeca(BigDecimal iznosPouzeca) {
        this.iznosPouzeca = iznosPouzeca;
    }

    public double getVisina() {
        return visina;
    }

    public void setVisina(double visina) {
        this.visina = visina;
    }

    public double getSirina() {
        return sirina;
    }

    public void setSirina(double sirina) {
        this.sirina = sirina;
    }

    public double getDuzina() {
        return duzina;
    }

    public void setDuzina(double duzina) {
        this.duzina = duzina;
    }

    public void setTezina(double tezina) {
        this.tezina = tezina;
    }

    public String getStatusIsporuke() {
        return statusIsporuke;
    }


    public LocalDateTime getVrijemePreuzimanja() {
        return vrijemePreuzimanja;
    }

    public void setVrijemePreuzimanja(LocalDateTime vrijemePreuzimanja) {
        this.vrijemePreuzimanja = vrijemePreuzimanja;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }

    public boolean isBeingDelivered() {
        return isBeingDelivered;
    }

    public void setBeingDelivered(boolean beingDelivered) {
        isBeingDelivered = beingDelivered;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public List<Observer> getObserverList() {
        return observerList;
    }

    public void setObserverList(List<Observer> observerList) {
        this.observerList = observerList;
    }

    public boolean isErrored() {
        return isErrored;
    }

    public void setErrored(boolean errored) {
        isErrored = errored;
    }
        @Override
    public String toString() {
        return "Paket{" +
                "oznaka='" + oznaka + '\'' +
                ", vrijemePrijema=" + getCroatianDate(vrijemePrijema) +
                ", vrstaPaketa=" + vrstaPaketa.getOznaka() +
                ", vrsta usluge=" + uslugaDostave +
                ", iznosPouzeca=" + calculatePrice() +
                ", iznosDostave=" + getVehiclePrice() +
                ", statusIsporuke='" + statusIsporuke + '\'' + '\n' +
                ", vrijemePreuzimanja=" + getCroatianDate(vrijemePreuzimanja) +
                '}';
    }


}
