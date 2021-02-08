package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PersonsRepositoryCollections implements PersonsRepository {

    NavigableMap<String, Person> persons = new TreeMap<>();

    @Override
    public Page<Person> findAll(Pageable pageable) {
        List<Person> personsFromData = new ArrayList<>();

        Iterator<Map.Entry<String, Person>> it = persons.entrySet().iterator();
        for (int i = 0; i < pageable.getOffset() && it.hasNext(); i++) {
            it.next();
        }

        int count = 0;
        while (count < pageable.getPageSize() && it.hasNext()) {
            personsFromData.add(it.next().getValue());
            count++;
        }

        return new PageImpl<>(personsFromData, pageable, persons.size());
    }

    @Override
    public Person save(Person s) {
        persons.put(s.getId(), s);
        return persons.get(s.getId());
    }

    @Override
    public Optional<Person> findById(String id) {
        return Optional.ofNullable(persons.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return persons.containsKey(id);
    }

    @Override
    public void deleteById(String id) {
        persons.remove(id);
    }
}
