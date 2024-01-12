package org.foi.uzdiz.mmusica.model;

import org.foi.uzdiz.mmusica.enums.TypeOfPackage;
import org.foi.uzdiz.mmusica.enums.TypeOfService;
import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.observer.Observer;
import org.foi.uzdiz.mmusica.observer.Subject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Paket implements Subject {
    private List<Observer> observerList = new ArrayList<>();
    private String oznaka;
    private LocalDateTime vrijemePrijema;
    private Person posiljatelj;
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
    private boolean isReceived;
    private boolean isBeingDelivered;
    private boolean isDelivered;
    private boolean isLoaded;
    private boolean isErrored;

    public Paket() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paket paket = (Paket) o;
        return Objects.equals(getOznaka(), paket.getOznaka());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOznaka());
    }

    @Override
    public void registerObserver(Observer observer) {
        if (!observerList.contains(observer)) this.observerList.add(observer);
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

    public double calculatePackageSize() {
        double packageSize;
        if (this.getVrstaPaketa().getOznaka().equals(TypeOfPackage.X.toString())) {
            packageSize = this.getVisina() * this.getSirina() * this.getDuzina();
        } else {
            packageSize = this.getVrstaPaketa().getDuzina() * this.getVrstaPaketa().getVisina() * this.getVrstaPaketa().getSirina();
        }
        return packageSize;
    }

    public BigDecimal calculatePrice() {
        BigDecimal basePrice = new BigDecimal(0);
        if (uslugaDostave.equals(TypeOfService.P.toString())) {
            return basePrice;
        }
        if (!uslugaDostave.equals(TypeOfService.H.toString())) {
            basePrice = vrstaPaketa.getCijena();
        } else {
            basePrice = vrstaPaketa.getCijenaHitno();
        }
        if (vrstaPaketa.getOznaka().equals(TypeOfPackage.X.toString())) {
            BigDecimal p = vrstaPaketa.getCijenaP().multiply(BigDecimal.valueOf(visina * sirina * duzina));
            BigDecimal t = vrstaPaketa.getCijenaT().multiply(BigDecimal.valueOf(tezina));
            basePrice = basePrice.add(p).add(t);

        }
        return basePrice;
    }

    public BigDecimal getVehiclePrice() {
        BigDecimal basePrice = new BigDecimal(0);
        if (uslugaDostave.equals(TypeOfService.P.toString())) {
            basePrice = basePrice.add(iznosPouzeca);
        }
        return basePrice;
    }

    public int getNajveciKbrUlicePrimatelja() {
        return this.primatelj.getUlica().getNajveciKbrUlice(primatelj.getUlica().getId());
    }

    public int getKbrPrimatelja() {
        return this.primatelj.getKbr();
    }

    public LocalDateTime getTimeOfReceival() {
        return vrijemePrijema;
    }

    private String getCroatianDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return dateTime.format(formatter);
    }

    public Location getDestination() {
        return primatelj.getArea();
    }

    public Location getDestinationStreet() {
        return primatelj.getUlica();
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

    public String getPackageString() {
        DecimalFormat df = new DecimalFormat("#,###.00");

        return String.format("%-5s | %-10s | %-10s | %-12s | %-12s | %-25s | %-20s | %-20s%n", vrstaPaketa.getOznaka(),
                uslugaDostave, oznaka, df.format(calculatePrice()), df.format(getVehiclePrice()), statusIsporuke,
                getCroatianDate(vrijemePrijema), getCroatianDate(vrijemePreuzimanja));
    }

    public String getWIPPackageString() {
        String posiljatelj;
        String primatelj;
        if (this.posiljatelj == null) {
            posiljatelj = "null";
        } else {
            posiljatelj = this.posiljatelj.getName();
        }

        if (this.primatelj == null) {
            primatelj = "null";
        } else {
            primatelj = this.primatelj.getName();
        }
        return String.format("%-10s | %-20s | %-20s%n", this.oznaka, posiljatelj, primatelj);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public PaketMemento takeSnapshot() {
        return new PaketMemento(observerList, oznaka, vrijemePrijema, posiljatelj,
                primatelj, vrstaPaketa, visina, sirina, duzina, tezina, uslugaDostave,
                iznosPouzeca, statusIsporuke, vrijemePreuzimanja, isReceived, isBeingDelivered, isDelivered, isLoaded,
                isErrored);
    }

    public void restore(PaketMemento memento) {
        observerList = memento.getObserverList();
        oznaka = memento.getOznaka();
        vrijemePrijema = memento.getVrijemePrijema();
        posiljatelj = memento.getPosiljatelj();
        primatelj = memento.getPrimatelj();
        vrstaPaketa = memento.getVrstaPaketa();
        visina = memento.getVisina();
        sirina = memento.getSirina();
        duzina = memento.getDuzina();
        tezina = memento.getTezina();
        uslugaDostave = memento.getUslugaDostave();
        iznosPouzeca = memento.getIznosPouzeca();
        statusIsporuke = memento.getStatusIsporuke();
        vrijemePreuzimanja = memento.getVrijemePreuzimanja();
        isReceived = memento.isReceived();
        isBeingDelivered = memento.isBeingDelivered();
        isDelivered = memento.isDelivered();
        isLoaded = memento.isLoaded();
        isErrored = memento.isErrored();
    }

    public static class PaketMemento {
        private final List<Observer> observerList;
        private final String oznaka;
        private final LocalDateTime vrijemePrijema;
        private final Person posiljatelj;
        private final Person primatelj;
        private final PackageType vrstaPaketa;
        private final double visina;
        private final double sirina;
        private final double duzina;
        private final double tezina;
        private final String uslugaDostave;
        private final BigDecimal iznosPouzeca;
        private final String statusIsporuke;
        private final LocalDateTime vrijemePreuzimanja;
        private final boolean isReceived;
        private final boolean isBeingDelivered;
        private final boolean isDelivered;
        private final boolean isLoaded;
        private final boolean isErrored;

        public PaketMemento(List<Observer> observerList, String oznaka, LocalDateTime vrijemePrijema,
                            Person posiljatelj, Person primatelj, PackageType vrstaPaketa, double visina, double sirina,
                            double duzina, double tezina, String uslugaDostave, BigDecimal iznosPouzeca, String statusIsporuke,
                            LocalDateTime vrijemePreuzimanja, boolean isReceived, boolean isBeingDelivered, boolean isDelivered,
                            boolean isLoaded, boolean isErrored) {
            this.observerList = new ArrayList<>(observerList);
            this.oznaka = oznaka;
            this.vrijemePrijema = vrijemePrijema;
            this.posiljatelj = posiljatelj;
            this.primatelj = primatelj;
            this.vrstaPaketa = vrstaPaketa;
            this.visina = visina;
            this.sirina = sirina;
            this.duzina = duzina;
            this.tezina = tezina;
            this.uslugaDostave = uslugaDostave;
            this.iznosPouzeca = iznosPouzeca;
            this.statusIsporuke = statusIsporuke;
            this.vrijemePreuzimanja = vrijemePreuzimanja;
            this.isReceived = isReceived;
            this.isBeingDelivered = isBeingDelivered;
            this.isDelivered = isDelivered;
            this.isLoaded = isLoaded;
            this.isErrored = isErrored;
        }

        private List<Observer> getObserverList() {
            return observerList;
        }

        private String getOznaka() {
            return oznaka;
        }

        private LocalDateTime getVrijemePrijema() {
            return vrijemePrijema;
        }

        private Person getPosiljatelj() {
            return posiljatelj;
        }

        private Person getPrimatelj() {
            return primatelj;
        }

        private PackageType getVrstaPaketa() {
            return vrstaPaketa;
        }

        private double getVisina() {
            return visina;
        }

        private double getSirina() {
            return sirina;
        }

        private double getDuzina() {
            return duzina;
        }

        private double getTezina() {
            return tezina;
        }

        private String getUslugaDostave() {
            return uslugaDostave;
        }

        private BigDecimal getIznosPouzeca() {
            return iznosPouzeca;
        }

        private String getStatusIsporuke() {
            return statusIsporuke;
        }

        private LocalDateTime getVrijemePreuzimanja() {
            return vrijemePreuzimanja;
        }

        private boolean isReceived() {
            return isReceived;
        }

        private boolean isBeingDelivered() {
            return isBeingDelivered;
        }

        private boolean isDelivered() {
            return isDelivered;
        }

        private boolean isLoaded() {
            return isLoaded;
        }

        private boolean isErrored() {
            return isErrored;
        }
    }
}
