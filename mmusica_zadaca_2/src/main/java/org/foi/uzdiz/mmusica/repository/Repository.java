package org.foi.uzdiz.mmusica.repository;

import java.util.List;

public interface Repository <T>{
    T save(T item);
    void saveAll (List<T> listOfItems);
    List <T> getAll();
}
