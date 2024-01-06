package org.foi.uzdiz.mmusica.model;

import java.math.BigDecimal;

public class PackageType {
    private String oznaka;
    private String opis;
    private double visina;
    private double sirina;
    private double duzina;
    private double maxTezina;
    private BigDecimal cijena;
    private BigDecimal cijenaHitno;
    private BigDecimal cijenaP;
    private BigDecimal cijenaT;


    public PackageType(String oznaka, String opis,
                       double visina, double sirina, double duzina, double maxTezina, BigDecimal cijena,
                       BigDecimal cijenaHitno, BigDecimal cijenaP, BigDecimal cijenaT) {
        this.oznaka = oznaka;
        this.opis = opis;
        this.visina = visina;
        this.sirina = sirina;
        this.duzina = duzina;
        this.maxTezina = maxTezina;
        this.cijena = cijena;
        this.cijenaHitno = cijenaHitno;
        this.cijenaP = cijenaP;
        this.cijenaT = cijenaT;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
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

    public double getMaxTezina() {
        return maxTezina;
    }

    public void setMaxTezina(double maxTezina) {
        this.maxTezina = maxTezina;
    }

    public BigDecimal getCijena() {
        return cijena;
    }

    public void setCijena(BigDecimal cijena) {
        this.cijena = cijena;
    }

    public BigDecimal getCijenaHitno() {
        return cijenaHitno;
    }

    public void setCijenaHitno(BigDecimal cijenaHitno) {
        this.cijenaHitno = cijenaHitno;
    }

    public BigDecimal getCijenaP() {
        return cijenaP;
    }

    public void setCijenaP(BigDecimal cijenaP) {
        this.cijenaP = cijenaP;
    }

    public BigDecimal getCijenaT() {
        return cijenaT;
    }

    public void setCijenaT(BigDecimal cijenaT) {
        this.cijenaT = cijenaT;
    }
}
