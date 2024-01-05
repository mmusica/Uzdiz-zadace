package org.foi.uzdiz.mmusica.voznja;

public class GPS {
    double lat;
    double lon;

    public GPS(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public static double distance(GPS from, GPS to) {
        double x1 = from.getLat();
        double x2 = to.getLat();
        double y1 = from.getLon();
        double y2 = to.getLon();
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    public boolean equals(GPS gps) {
        return gps.lat == this.lat && gps.lon == this.lon;
    }
}
