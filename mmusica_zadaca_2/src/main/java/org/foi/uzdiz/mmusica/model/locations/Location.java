package org.foi.uzdiz.mmusica.model.locations;

public interface Location {
    void display();
    Long getId();
    Location findStreet(long id);

}
