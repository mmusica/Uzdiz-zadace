package org.foi.uzdiz.mmusica.model.locations;

import java.util.List;

public class Place implements Location {
    private List<Location> locationList;
    private Long id;
    private String naziv;

    @Override
    public void display() {
        System.out.println("\tGrad/Mjesto "+this.id+ " "+this.naziv+".");
        this.locationList.forEach(Location::display);
        System.out.println("\t------------------------------------------------------------------------------------------------------------------------");
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

    @Override
    public Location findStreet(long id) {
        for (Location l : locationList) {
            Location location = l.findStreet(id);
            if (location != null) return location;
        }
        return null;
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
