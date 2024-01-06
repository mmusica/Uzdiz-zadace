package org.foi.uzdiz.mmusica.voznja;

import org.foi.uzdiz.mmusica.model.Paket;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Segment {
    private GPS gpsFrom;
    private GPS gpsTo;
    private double distance;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private double driveDuration;
    private double deliveryDuration;
    private double totalSegmentTime;
    private Paket paket;

    public Segment(GPS gpsFrom, GPS gpsTo, double distance,
                   LocalDateTime startTime, LocalDateTime finishTime, double driveDuration, double deliveryDuration, Paket paket) {
        this.gpsFrom = gpsFrom;
        this.gpsTo = gpsTo;
        this.distance = distance;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.driveDuration = driveDuration;
        this.deliveryDuration = deliveryDuration;
        this.paket = paket;
    }

    public GPS getGpsFrom() {
        return gpsFrom;
    }

    public void setGpsFrom(GPS gpsFrom) {
        this.gpsFrom = gpsFrom;
    }

    public GPS getGpsTo() {
        return gpsTo;
    }

    public void setGpsTo(GPS gpsTo) {
        this.gpsTo = gpsTo;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public double getDriveDuration() {
        return driveDuration;
    }

    public void setDriveDuration(double driveDuration) {
        this.driveDuration = driveDuration;
    }

    public double getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(double deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public double getTotalSegmentTime() {
        return this.driveDuration + this.deliveryDuration;
    }

    public void setTotalSegmentTime(double totalSegmentTime) {
        this.totalSegmentTime = totalSegmentTime;
    }

    public Paket getPaket() {
        return paket;
    }

    public void setPaket(Paket paket) {
        this.paket = paket;
    }
}