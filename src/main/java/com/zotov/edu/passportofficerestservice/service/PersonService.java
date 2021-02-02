package com.zotov.edu.passportofficerestservice.service;

import com.zotov.edu.passportofficerestservice.converter.PersonConverter;
import com.zotov.edu.passportofficerestservice.exception.EntityNotFoundException;
import com.zotov.edu.passportofficerestservice.model.entity.Person;
import com.zotov.edu.passportofficerestservice.model.request.PersonRequest;
import com.zotov.edu.passportofficerestservice.model.response.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.PassportsRepositoryCollections;
import com.zotov.edu.passportofficerestservice.repository.PersonsRepositoryCollections;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonsRepositoryCollections personsRepository;

    private final PassportsRepositoryCollections passportsRepository;

    private final PersonConverter personConverter;

    public Page<PersonResponse> getAllPersons(Pageable pageable) {
        Page<Person> personPage = personsRepository.findAll(pageable);
        return personConverter.convertToDto(personPage);
    }

    public PersonResponse createPerson(PersonRequest personRequest) {
        Person person = personConverter.convertToEntity(personRequest);
        Person createdPerson = personsRepository.save(person);
        return personConverter.convertToDto(createdPerson);
    }

    public PersonResponse getPerson(String id) {
        return personsRepository
                .findById(id)
                .map(personConverter::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public PersonResponse updatePerson(String id, PersonRequest personRequest) {
        checkIfPersonExists(id);
        Person person = personConverter.convertToEntity(id, personRequest);
        Person updatedPerson = personsRepository.save(person);
        return personConverter.convertToDto(updatedPerson);
    }

    public Page<PersonResponse> getPersonByPassportNumber(String passportNumber) {
        List<PersonResponse> foundPersons = new ArrayList<>();
        passportsRepository
                .findById(passportNumber)
                .ifPresent(passport -> foundPersons.add(getPerson(passport.getOwnerId())));
        return new PageImpl<>(foundPersons);
    }

    public void deletePersonById(String id) {
        checkIfPersonExists(id);
        personsRepository.deleteById(id);
    }

    public PersonService checkIfPersonExists(String id) {
        if (!personsRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }
        return this;
    }

}
