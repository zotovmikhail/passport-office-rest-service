package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.repository.PersonsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;

@Component
@AllArgsConstructor
public class PersonDataHandler {

    private final PersonsRepository personsRepository;

    public void generatePersonsData(int numberOfPersons) {
        personsRepository.saveAll(generatePersons(numberOfPersons));
    }

    public Person generatePersonData() {
        return personsRepository.create(generatePerson());
    }

}
