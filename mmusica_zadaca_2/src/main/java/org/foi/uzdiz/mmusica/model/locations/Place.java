package org.foi.uzdiz.mmusica.model.locations;

import java.util.ArrayList;
import java.util.List;

public class Place implements Location {
    private List<Location> locationList = new ArrayList<>();
    private Long id;
    private String naziv;
    @Override
    public void display() {

    }

    public Place(Long id, String naziv, List<Location> locationList) {
        this.locationList = locationList;
        this.id = id;
        this.naziv = naziv;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    @Override
    public Long getId() {
        return id;
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
}
