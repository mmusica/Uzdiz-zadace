package org.foi.uzdiz.mmusica.proxy;

import org.foi.uzdiz.mmusica.model.Person;
import org.foi.uzdiz.mmusica.observer.Observer;
import org.foi.uzdiz.mmusica.observer.Subject;

public class PackageSubscriberProxy implements PackageSubscriber {

    PackageSubscriber packageSubscriberImpl;

    public PackageSubscriberProxy(PackageSubscriber packageSubscriberImpl) {
        this.packageSubscriberImpl = packageSubscriberImpl;
    }

    @Override
    public void subscribe(Subject paket, Observer observer) {
        if(observer instanceof Person person){
            if (!person.isAdmin()) {
                System.out.println("PROXY ERROR: "+observer.getName()+" nije admin");
                return;
            }
        }
        packageSubscriberImpl.subscribe(paket,observer);
    }

    @Override
    public void unsubscribe(Subject paket, Observer observer) {
        if(observer instanceof Person person){
            if (!person.isAdmin()) {
                System.out.println("PROXY ERROR: "+observer.getName()+" nije admin");
                return;
            }
        }
        packageSubscriberImpl.unsubscribe(paket,observer);
    }

    public PackageSubscriber getPackageSubscriberImpl() {
        return packageSubscriberImpl;
    }

    public void setPackageSubscriberImpl(PackageSubscriber packageSubscriberImpl) {
        this.packageSubscriberImpl = packageSubscriberImpl;
    }
}
