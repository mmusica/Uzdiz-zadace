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
    public static double distance(GPS from, GPS to){
        double x1 = from.getLat();
        double x2 = to.getLat();
        double y1 = from.getLon();
        double y2 = from.getLon();
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
}
