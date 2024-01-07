package org.foi.uzdiz.mmusica.utils.responsibility_chain;

import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.Person;
import org.foi.uzdiz.mmusica.proxy.PackageSubscriber;
import org.foi.uzdiz.mmusica.proxy.PackageSubscriberImpl;
import org.foi.uzdiz.mmusica.proxy.PackageSubscriberProxy;
import org.foi.uzdiz.mmusica.repository.singleton.RepositoryManager;

import java.util.List;

public class UnProxyCommandHandler extends UserCommandHandler{
    public UnProxyCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {
        Person personFromProxyCommand = getPersonFromAdminCommand(commandArray);
        if (personFromProxyCommand != null) {
            unsubscribeToAllPackages(personFromProxyCommand);
        } else {
            System.out.println("Osoba ne postoji");
        }
    }

    @Override
    String getCommand() {
        return "UNPROXY";
    }

    private void unsubscribeToAllPackages(Person personFromProxyCommand) {
        PackageSubscriber packageSubscriber = new PackageSubscriberProxy(new PackageSubscriberImpl());
        List<Paket> allPackages = RepositoryManager.getINSTANCE().getPackageRepository().getAll();
        allPackages.forEach(paket -> {
            packageSubscriber.unsubscribe(paket, personFromProxyCommand);
        });
    }
}
