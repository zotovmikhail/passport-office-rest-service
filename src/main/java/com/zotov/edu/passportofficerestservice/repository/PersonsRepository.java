package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.model.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PersonsRepository {
    Page<Person> findAll(Pageable pageable);

    Person save(Person s);

    Optional<Person> findById(String id);

    boolean existsById(String id);

    void deleteById(String id);
}