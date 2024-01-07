package org.foi.uzdiz.mmusica.voznja;

import org.foi.uzdiz.mmusica.enums.TypeOfService;
import org.foi.uzdiz.mmusica.model.Paket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//MORAS PROTOTYPE
public class Drive implements Cloneable{
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

    public double getDistance() {
        double distance = 0;
        for (Segment segment : segments) {
            distance += segment.getDistance();
        }
        return distance;
    }

    public LocalDateTime getStartTime() {
        Segment segment = segments.get(0);
        return segment.getStartTime();
    }

    public LocalDateTime getReturnTime() {
        //Uvijek ce biti samo jedan paket = null segment i to zadnji
        List<Segment> list = segments.stream().filter(segment -> segment.getPaket() == null).toList();
        if (list.isEmpty()) return null;
        else return list.get(0).getFinishTime();
    }

    public LocalDateTime getFinishTimeOfLastSegment() {
        //Uvijek ce biti samo jedan paket = null segment i to zadnji
        return this.segments.get(segments.size() - 1).getFinishTime();
    }

    public String getUkupanBrojPaketaPoVrstiString() {
        StringBuilder stringBuilder = new StringBuilder();
        int brojacHitni = 0;
        int brojacObicni = 0;
        int brojIsporucenih = 0;
        //svi paketi u segmentima koji nisu null - null je samo zadnji segment
        List<Paket> paketi = segments.stream().map(Segment::getPaket).filter(Objects::nonNull).toList();
        for (Paket p : paketi) {
            if (p.getUslugaDostave().equals(TypeOfService.H.toString())) {
                brojacHitni++;
            } else {
                brojacObicni++;
            }
            if(p.isDelivered()){
                brojIsporucenih++;
            }
        }

        stringBuilder.append(brojacHitni).append(" HITNIH, ").append(brojacObicni).append(" NORMALNIH, ").append(brojIsporucenih).append(" DOSTAVLJENIH");
        return stringBuilder.toString();
    }

    @Override
    public Drive clone() {
        try {
            Drive clone = (Drive) super.clone();
            List<Segment> segments1 = new ArrayList<>(segments);
            clone.setSegments(segments1);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
