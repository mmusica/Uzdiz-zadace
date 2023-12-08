package org.foi.uzdiz.mmusica.voznja;

import org.foi.uzdiz.mmusica.model.Paket;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Segment {
    GPS gpsOd;
    GPS gpsDo;
    float udaljenost;
    LocalDateTime vrijemePocetka;
    LocalTime vrijemeKraja;
    LocalTime trajanjeVoznje;
    LocalTime trajanjeIsporuke;
    LocalTime ukupnoTrajanjeSegmenta;
    Paket paket;
}
