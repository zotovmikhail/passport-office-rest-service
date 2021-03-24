package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PersonsRepository {
    Page<Person> findAll(Pageable pageable);

    Person create(Person person);

    Person save(Person s);

    Optional<Person> findById(String id);

    boolean existsById(String id);

    void deleteById(String id);

    void saveAll(List<Person> persons);
}
