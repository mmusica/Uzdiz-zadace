package org.foi.uzdiz.mmusica.repository;

import org.foi.uzdiz.mmusica.model.PackageType;

import java.util.ArrayList;
import java.util.List;

public class PackageTypeRepository implements Repository<PackageType> {

    private final List<PackageType> packageTypes = new ArrayList<>();

    public PackageTypeRepository() {
    }
    @Override
    public PackageType save(PackageType item) {
        packageTypes.add(item);
        return packageTypes.get(packageTypes.size()-1);
    }

    @Override
    public void saveAll(List<PackageType> listOfItems) {
        packageTypes.addAll(listOfItems);
    }

    @Override
    public List<PackageType> getAll() {
        return packageTypes;
    }
}
