package org.foi.uzdiz.mmusica.repository;


import org.foi.uzdiz.mmusica.model.Paket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class PackageRepository implements Repository<Paket> {

    private final List<Paket> listaPaketi = new ArrayList<>();

    public PackageRepository() {

    }

    @Override
    public List<Paket> getAll() {
        return listaPaketi.stream().sorted(Comparator.comparing(Paket::getTimeOfReceival)).toList();
    }

    @Override
    public <J> Paket find(J id) {
        return null;
    }


    @Override
    public Paket save(Paket item) {
        listaPaketi.add(item);
        return listaPaketi.get(listaPaketi.size() - 1);
    }

    @Override
    public void saveAll(List<Paket> listOfItems) {
        listaPaketi.addAll(listOfItems);
    }
}
