package org.foi.uzdiz.mmusica.voznja;

import java.util.ArrayList;
import java.util.List;

public class Drive {
    List<Segment> segments;
    public Drive() {
        this.segments = new ArrayList<>();
    }
    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
