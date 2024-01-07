package org.foi.uzdiz.mmusica.proxy;

import org.foi.uzdiz.mmusica.observer.Observer;
import org.foi.uzdiz.mmusica.observer.Subject;

public interface PackageSubscriber {
    void subscribe(Subject paket, Observer observer);
    void unsubscribe(Subject paket, Observer observer);
}
