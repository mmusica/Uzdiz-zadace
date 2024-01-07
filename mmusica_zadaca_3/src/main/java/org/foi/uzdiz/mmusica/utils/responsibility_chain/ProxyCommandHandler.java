package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Person;
import org.foi.uzdiz.mmusica.proxy.PackageSubscriber;
import org.foi.uzdiz.mmusica.proxy.PackageSubscriberImpl;
import org.foi.uzdiz.mmusica.proxy.PackageSubscriberProxy;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;

import java.util.List;

public class ProxyCommandHandler extends UserCommandHandler{
    public ProxyCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {

        Person personFromProxyCommand = getPersonFromAdminCommand(commandArray);
        if (personFromProxyCommand != null) {
            subscribeToAllPackages(personFromProxyCommand);
        } else {
            System.out.println("Osoba ne postoji");
        }
    }

    @Override
    String getCommand() {
        return "PROXY";
    }
    private void subscribeToAllPackages(Person person) {
        PackageSubscriber packageSubscriber = new PackageSubscriberProxy(new PackageSubscriberImpl());
        List<Paket> allPackages = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        allPackages.forEach(paket -> {
            packageSubscriber.subscribe(paket, person);
        });
    }
}
