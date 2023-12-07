package org.foi.uzdiz.mmusica.model;

import org.foi.uzdiz.mmusica.model.locations.Location;
import org.foi.uzdiz.mmusica.observer.Observer;
import org.foi.uzdiz.mmusica.observer.Subject;


public class Person implements Observer {
    private String name;
    private Location area;
    private Location grad;
    private Location ulica;
    private int kbr;

    @Override
    public void update(Subject subject) {
        System.out.println(this.name + ": Paket --> " + subject.getStatus());
    }

    public Person(String name, Location area, Location grad, Location ulica, int kbr) {
        this.name = name;
        this.grad = grad;
        this.area = area;
        this.ulica = ulica;
        this.kbr = kbr;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getGrad() {
        return grad;
    }

    public void setGrad(Location grad) {
        this.grad = grad;
    }

    public Location getUlica() {
        return ulica;
    }

    public void setUlica(Location ulica) {
        this.ulica = ulica;
    }

    public int getKbr() {
        return kbr;
    }

    public void setKbr(int kbr) {
        this.kbr = kbr;
    }

    public Location getArea() {
        return area;
    }

    public void setArea(Location area) {
        this.area = area;
    }
}
