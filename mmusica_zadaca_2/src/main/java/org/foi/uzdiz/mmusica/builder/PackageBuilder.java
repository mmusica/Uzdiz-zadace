package org.foi.uzdiz.mmusica.builder;

import org.foi.uzdiz.mmusica.model.PackageType;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.observer.Observer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PackageBuilder {
    Paket build();

    PackageBuilder oznaka(String oznaka);

    PackageBuilder posiljatelj(String posiljatelj);

    PackageBuilder primatelj(String primatelj);

    PackageBuilder vrijemePrijema(LocalDateTime vrijemePrijema);

    PackageBuilder visina(double visina);

    PackageBuilder sirina(double sirina);

    PackageBuilder duzina(double duzina);

    PackageBuilder tezina(double tezina);

    PackageBuilder uslugaDostave(String uslugaDostave);

    PackageBuilder iznosPouzeca(BigDecimal iznosPouzeca);

    PackageBuilder statusIsporuke(String statusIsporuke);

    PackageBuilder vrijemePreuzimanja(LocalDateTime vrijemePreuzimanja);

    PackageBuilder isReceived(boolean isReceived);

    PackageBuilder isBeingDelivered(boolean isBeingDelivered);

    PackageBuilder isDelivered(boolean isDelivered);

    PackageBuilder vrstaPaketa(PackageType packageType);
    PackageBuilder observerList(List<Observer> observerList);
    PackageBuilder isErrored(boolean isErrored);

}
