package org.foi.uzdiz.mmusica.repository;

import org.foi.uzdiz.mmusica.model.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonRepository implements Repository<Person> {

    List<Person> personList = new ArrayList<>();
    @Override
    public Person save(Person item) {
        personList.add(item);
        return personList.get(personList.size()-1);
    }

    @Override
    public void saveAll(List<Person> listOfItems) {
        personList.addAll(listOfItems);
    }

    @Override
    public List<Person> getAll() {
        return personList;
    }

    @Override
    public <J> Person find(J id) {
        List<Person> list = personList.stream().filter(person -> person.getName().equals(id)).toList();
        if(list.isEmpty() || (long) list.size() >1) return null;
        else return list.get(0);
    }
}
