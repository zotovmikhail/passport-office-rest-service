package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.service.exception.PersonNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(value = "application.repository", havingValue = "memory")
public class PersonsRepositoryCollections implements PersonsRepository {

    private final Map<String, Person> persons = new LinkedHashMap<>();

    @Override
    public Page<Person> findAll(Pageable pageable) {
        List<Person> personsFromData = persons.entrySet()
                .stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        return new PageImpl<>(personsFromData, pageable, persons.size());
    }

    @Override
    public Person create(Person person) {
        return save(person);
    }

    @Override
    public Person save(Person person) {
        persons.put(person.getId(), person);
        return person;
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
        if (persons.remove(id) == null) {
            throw new PersonNotFoundException(id);
        }
    }

    @Override
    public void saveAll(List<Person> personsToAdd) {
        Map<String, Person> personsMap = personsToAdd.stream()
                .collect(Collectors.toMap(Person::getId, person -> person));
        persons.putAll(personsMap);
    }
}
