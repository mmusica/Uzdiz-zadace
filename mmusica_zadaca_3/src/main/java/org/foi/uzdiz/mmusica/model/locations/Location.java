package org.foi.uzdiz.mmusica.model.locations;

import org.foi.uzdiz.mmusica.voznja.GPS;

public interface Location {
    void display();
    Long getId();
    Location findStreet(long id);
    GPS getStartOfStreet(long id);
    GPS getEndOfStreet(long id);
    int getNajveciKbrUlice(long id);
}
