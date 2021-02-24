package com.zotov.edu.passportofficerestservice.service;

import com.zotov.edu.passportofficerestservice.repository.PassportsRepositoryCollections;
import com.zotov.edu.passportofficerestservice.repository.PersonsRepositoryCollections;
import com.zotov.edu.passportofficerestservice.repository.converter.PersonEntityConverter;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.service.exception.PersonNotFoundException;
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

    public Person getPerson(String personId) {
        return personsRepository
                .findById(personId)
                .orElseThrow(() -> new PersonNotFoundException(personId));
    }

    public Person updatePerson(String personId, String name, LocalDate birthday, String country) {
        checkIfPersonExists(personId);
        Person person = personConverter.convertToEntity(personId, name, birthday, country);
        return personsRepository.save(person);
    }

    public Page<Person> getPersonByPassportNumber(String passportNumber, Pageable pageable) {
        List<Person> foundPersons = new ArrayList<>();
        passportsRepository
                .findByPassportNumber(passportNumber)
                .ifPresent(passport -> foundPersons.add(getPerson(passport.getOwnerId())));
        return new PageImpl<>(foundPersons, pageable, foundPersons.size());
    }

    public void deletePersonById(String personId) {
        passportsRepository.deleteAllByOwnerId(personId);
        personsRepository.deleteById(personId);
    }

    public void checkIfPersonExists(String personId) {
        if (!personsRepository.existsById(personId)) {
            throw new PersonNotFoundException(personId);
        }
    }

}
