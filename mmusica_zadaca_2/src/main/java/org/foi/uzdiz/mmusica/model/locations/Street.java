package org.foi.uzdiz.mmusica.model.locations;

public class Street implements Location {
    private Long id;
    private String naziv;
    private float lat1;
    private float lon1;
    private float lat2;
    private float lon2;
    private int najveciKucniBroj;

    @Override
    public void display() {
        System.out.printf("\t\tUlica: id: %d, naziv: %s, lat1: %f, lon1: %f, lat2: %f, lon2: %f, nkbr: %o\n", id,naziv,lat1,lon1,lat2,lon2,najveciKucniBroj);
    }

    public Street(Long id, String naziv, float lat1, float lon1, float lat2, float lon2, int najveciKucniBroj) {
        this.id = id;
        this.naziv = naziv;
        this.lat1 = lat1;
        this.lon1 = lon1;
        this.lat2 = lat2;
        this.lon2 = lon2;
        this.najveciKucniBroj = najveciKucniBroj;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Location findStreet(long id) {
        if (this.id == id) return this;
        else return null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public float getLat1() {
        return lat1;
    }

    public void setLat1(float lat1) {
        this.lat1 = lat1;
    }

    public float getLon1() {
        return lon1;
    }

    public void setLon1(float lon1) {
        this.lon1 = lon1;
    }

    public float getLat2() {
        return lat2;
    }

    public void setLat2(float lat2) {
        this.lat2 = lat2;
    }

    public float getLon2() {
        return lon2;
    }

    public void setLon2(float lon2) {
        this.lon2 = lon2;
    }

    public int getNajveciKucniBroj() {
        return najveciKucniBroj;
    }

    public void setNajveciKucniBroj(int najveciKucniBroj) {
        this.najveciKucniBroj = najveciKucniBroj;
    }
}
