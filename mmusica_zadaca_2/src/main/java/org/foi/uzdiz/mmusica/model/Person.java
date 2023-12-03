package org.foi.uzdiz.mmusica.model;

import org.foi.uzdiz.mmusica.observer.Observer;
import org.foi.uzdiz.mmusica.observer.Subject;

public class Person implements Observer {
    String name;
    @Override
    public void update(Subject subject) {
        System.out.println(subject.getStatus());
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
