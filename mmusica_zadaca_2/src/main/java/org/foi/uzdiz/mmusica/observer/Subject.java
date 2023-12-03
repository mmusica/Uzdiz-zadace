package org.foi.uzdiz.mmusica.observer;

public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
    String getStatus();
    String setStatus();
}
