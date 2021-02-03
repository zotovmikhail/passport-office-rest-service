package com.zotov.edu.passportofficerestservice.service;

import com.zotov.edu.passportofficerestservice.repository.PassportsRepositoryCollections;
import com.zotov.edu.passportofficerestservice.repository.PersonsRepositoryCollections;
import com.zotov.edu.passportofficerestservice.repository.converter.PersonEntityConverter;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.service.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonsRepositoryCollections personsRepository;

    private final PassportsRepositoryCollections passportsRepository;

    private final PersonEntityConverter personConverter;

    public Page<Person> getAllPersons(Pageable pageable) {
        return personsRepository.findAll(pageable);
    }

    public Person createPerson(String name, LocalDate birthday, String country) {
        Person person = personConverter.convertToEntity(name, birthday, country);
        return personsRepository.save(person);
    }

    public Person getPerson(String id) {
        return personsRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public Person updatePerson(String id, String name, LocalDate birthday, String country) {
        checkIfPersonExists(id);
        Person person = personConverter.convertToEntity(id, name, birthday, country);
        return personsRepository.save(person);
    }

    public Page<Person> getPersonByPassportNumber(String passportNumber) {
        List<Person> foundPersons = new ArrayList<>();
        passportsRepository
                .findById(passportNumber)
                .ifPresent(passport -> foundPersons.add(getPerson(passport.getOwnerId())));
        return new PageImpl<>(foundPersons);
    }

    public void deletePersonById(String id) {
        checkIfPersonExists(id);
        personsRepository.deleteById(id);
    }

    public void checkIfPersonExists(String id) {
        if (!personsRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }
    }

}
