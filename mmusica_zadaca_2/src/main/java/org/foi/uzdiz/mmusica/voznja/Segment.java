package org.foi.uzdiz.mmusica.voznja;

import org.foi.uzdiz.mmusica.model.Paket;

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
    private long driveDuration;
    private long deliveryDuration;
    private long totalSegmentTime;
    private Paket paket;

    public Segment(GPS gpsFrom, GPS gpsTo, double distance, LocalDateTime startTime,
                   LocalDateTime finishTime, long driveDuration, long deliveryDuration, Paket paket) {
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
        return this.finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public double getDriveDuration() {
        return driveDuration;
    }

    public void setDriveDuration(long driveDuration) {
        this.driveDuration = driveDuration;
    }

    public long getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public long getTotalSegmentTime() {
        return driveDuration + deliveryDuration;
    }

    public void setTotalSegmentTime(long totalSegmentTime) {
        this.totalSegmentTime = totalSegmentTime;
    }

    public Paket getPaket() {
        return paket;
    }

    public void setPaket(Paket paket) {
        this.paket = paket;
    }
}
