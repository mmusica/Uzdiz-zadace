package org.foi.uzdiz.mmusica.builder;

import org.foi.uzdiz.mmusica.model.PackageType;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.repository.PackageTypeRepository;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.RepositoryManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PackageBuildDirector {
    private final PackageBuilder packageBuilder;
    private static final int OZNAKA = 0;
    private static final int VRIJEME_PRIJEMA = 1;
    private static final int POSILJATELJ = 2;
    private static final int PRIMATELJ = 3;
    private static final int VRSTA_PAKETA = 4;
    private static final int VISINA = 5;
    private static final int SIRINA = 6;
    private static final int DUZINA = 7;
    private static final int TEZINA = 8;
    private static final int USLUGA_DOSTAVE = 9;
    private static final int IZNOS_POUZECA = 10;
    public PackageBuildDirector(PackageBuilder packageBuilder) {
        this.packageBuilder = packageBuilder;
    }
    public Paket constructPackage(String[] a){
        return packageBuilder.oznaka(a[OZNAKA])
                .vrijemePrijema(getVrijemePrijema(a[VRIJEME_PRIJEMA]))
                .posiljatelj(a[POSILJATELJ])
                .primatelj(a[PRIMATELJ])
                .vrstaPaketa(getPackageType(a[VRSTA_PAKETA]))
                .visina(Double.parseDouble(getValidValue(a[VISINA])))
                .sirina(Double.parseDouble(getValidValue(a[SIRINA])))
                .duzina(Double.parseDouble(getValidValue(a[DUZINA])))
                .tezina(Double.parseDouble(getValidValue(a[TEZINA])))
                .uslugaDostave(a[USLUGA_DOSTAVE])
                .iznosPouzeca(getIznosPouzeca(a))
                .statusIsporuke("Jos nije preuzet")
                .isBeingDelivered(false)
                .isReceived(false)
                .isDelivered(false)
                .build();
    }

    private LocalDateTime getVrijemePrijema(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return LocalDateTime.parse(s.trim(), formatter);
    }
    private BigDecimal getIznosPouzeca(String[] a) {
        return new BigDecimal(a[IZNOS_POUZECA].replace(",", ".").trim());
    }
    private String getValidValue (String s) {
        return s.replace(',','.').trim();
    }
    private PackageType getPackageType(String s) {
        Repository<PackageType> packageTypeRepository = RepositoryManager.getINSTANCE().getPackageTypeRepository();
        return packageTypeRepository.getAll().stream().filter(packageType -> packageType.getOznaka().equals(s)).toList().get(0);
    }
}
