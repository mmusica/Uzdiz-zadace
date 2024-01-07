package org.foi.uzdiz.mmusica.proxy;

import org.foi.uzdiz.mmusica.observer.Observer;
import org.foi.uzdiz.mmusica.observer.Subject;

public class PackageSubscriberImpl implements PackageSubscriber{
    @Override
    public void subscribe(Subject paket, Observer observer) {
        paket.registerObserver(observer);
    }

    @Override
    public void unsubscribe(Subject paket, Observer observer) {
        paket.removeObserver(observer);
    }
}
