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

    //Preuzeto s https://www.geodatasource.com/developers/java
    public static double distanceInKM(GPS from, GPS to) {
        double lat1 = from.getLat();
        double lon1 = from.getLon();
        double lat2 = to.getLat();
        double lon2 = to.getLon();
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) *
                    Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return (dist);
        }
    }

    public boolean equals(GPS gps) {
        return gps.lat == this.lat && gps.lon == this.lon;
    }
}
