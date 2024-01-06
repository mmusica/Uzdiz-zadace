package org.foi.uzdiz.mmusica.builder;

import org.foi.uzdiz.mmusica.model.PackageType;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Person;
import org.foi.uzdiz.mmusica.observer.Observer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PackageBuilderImpl implements PackageBuilder {
    private Paket paket = new Paket();

    @Override
    public Paket build() {
        return this.paket;
    }

    @Override
    public PackageBuilder oznaka(String oznaka) {
        this.paket.setOznaka(oznaka);
        return this;
    }

    @Override
    public PackageBuilder posiljatelj(Person posiljatelj) {
        this.paket.setPosiljatelj(posiljatelj);
        return this;
    }

    @Override
    public PackageBuilder primatelj(Person primatelj) {
        this.paket.setPrimatelj(primatelj);
        return this;
    }

    @Override
    public PackageBuilder vrijemePrijema(LocalDateTime vrijemePrijema) {
        this.paket.setVrijemePrijema(vrijemePrijema);
        return this;
    }

    @Override
    public PackageBuilder visina(double visina) {
        this.paket.setVisina(visina);
        return this;
    }

    @Override
    public PackageBuilder sirina(double sirina) {
        this.paket.setSirina(sirina);
        return this;
    }

    @Override
    public PackageBuilder duzina(double duzina) {
        this.paket.setDuzina(duzina);
        return this;
    }

    @Override
    public PackageBuilder tezina(double tezina) {
        this.paket.setTezina(tezina);
        return this;
    }

    @Override
    public PackageBuilder uslugaDostave(String uslugaDostave) {
        this.paket.setUslugaDostave(uslugaDostave);
        return this;
    }

    @Override
    public PackageBuilder iznosPouzeca(BigDecimal iznosPouzeca) {
        this.paket.setIznosPouzeca(iznosPouzeca);
        return this;
    }

    @Override
    public PackageBuilder statusIsporuke(String statusIsporuke) {
        this.paket.setStatusIsporuke(statusIsporuke);
        return this;
    }

    @Override
    public PackageBuilder vrijemePreuzimanja(LocalDateTime vrijemePreuzimanja) {
        this.paket.setVrijemePreuzimanja(vrijemePreuzimanja);
        return this;
    }

    @Override
    public PackageBuilder isReceived(boolean isReceived) {
        this.paket.setReceived(isReceived);
        return this;
    }

    @Override
    public PackageBuilder isBeingDelivered(boolean isBeingDelivered) {
        this.paket.setBeingDelivered(isBeingDelivered);
        return this;
    }

    @Override
    public PackageBuilder isDelivered(boolean isDelivered) {
        this.paket.setDelivered(isDelivered);
        return this;
    }

    @Override
    public PackageBuilder vrstaPaketa(PackageType packageType) {
        this.paket.setVrstaPaketa(packageType);
        return this;
    }

    @Override
    public PackageBuilder observerList(List<Observer> observerList) {
        this.paket.getObserverList().addAll(observerList);
        return this;
    }

    @Override
    public PackageBuilder isErrored(boolean isErrored) {
        this.paket.setErrored(isErrored);
        return this;
    }

    @Override
    public PackageBuilder isLoaded(boolean isLoaded) {
       this.paket.setLoaded(isLoaded);
       return this;
    }
}
