package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.repository.PersonsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import lombok.Getter;
import org.springframework.stereotype.Component;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePerson;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePersons;

@Component
public class PersonDataHandler {

    @Getter
    private final PersonsRepository personsRepository;

    public PersonDataHandler(PersonsRepository personsRepository) {
        this.personsRepository = personsRepository;
    }

    public void generatePersonsData(int numberOfPersons) {
        personsRepository.saveAll(generatePersons(numberOfPersons));
    }

    public Person generatePersonData() {
        return personsRepository.save(generatePerson());
    }

}
